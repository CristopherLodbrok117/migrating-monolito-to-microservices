package app.calendar_service.application.services;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.usecases.InvitationTokenService;
import app.calendar_service.application.usecases.MailService;
import app.calendar_service.domain.entities.*;
import app.calendar_service.domain.exception.EmailException;
import app.calendar_service.domain.policies.EventPolicy;

import app.calendar_service.domain.policies.GroupPolicy;
import app.calendar_service.application.repositories.GroupRepository;
import app.calendar_service.application.repositories.MemberRoleRepository;
import app.calendar_service.application.repositories.MembershipRepository;
import app.calendar_service.application.repositories.UserRepository;
import app.calendar_service.application.usecases.GroupService;
import app.calendar_service.web.dtos.*;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final MemberRoleRepository roleRepository;

    private final InvitationTokenService invitationTokenService;

    private final GroupPolicy groupPolicy;

    private final EventPolicy eventPolicy;

    private final MailServiceImpl mailService;

    @Override
    @Transactional(readOnly = true)
    public List<GroupDto> findAll() {
        return groupRepository.findAll().stream()
                .map(GroupDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pair<GroupDto, UserPermissionsDto>> findOneWithDetails(Long id) {
        groupPolicy.authorize("view", GroupDto.builder().id(id).build());

        Optional<Group> groupDB =  groupRepository.findByIdWithDetails(id);

        Optional<GroupDto> groupDto = groupDB
                .map( group -> GroupDto.fromEntity(group)
                        .withMembersFromMemberships(group.getMemberships())
                        .withEventsAndActivities(group.getEvents())
                )
                .or(Optional::empty);

        User authUser = groupPolicy.getAuthenticatedUser();

        Group group = groupDB.orElseThrow();
        UserPermissionsDto userPermissions = UserPermissionsDto.builder()
                .addMembers(groupPolicy.addMembers(authUser, group))
                .updateName(groupPolicy.updateName(authUser, group))
                .manageCategories(groupPolicy.manageCategories(authUser,group))
                .kickMembers(groupPolicy.kickAnyMember(authUser,group))
                .kickAnyMember(groupPolicy.kickAnyMember(authUser, group))
                .assignRoles(groupPolicy.delete(authUser,group))
                .createEvents(eventPolicy.create(authUser, group))
                .viewEvents(eventPolicy.view(authUser, group))
                .build();

        return  Optional.of(new Pair<>(groupDto.orElseThrow(), userPermissions));
    }

    @Override
    @Transactional
    public Optional<GroupDto> update(Long groupId, GroupDto groupDto) {
        Group group = groupRepository.findById(groupId).orElseThrow();

        groupPolicy.authorize("updateName", group);

        group.setName(groupDto.getName());
        groupRepository.save(group);

        return Optional.of(GroupDto.fromEntity(group));
    }

    @Override
    @Transactional
    public Optional<GroupDto> save(String creatorUsername, GroupDto groupDto) {
        User creator = userRepository.findOneByUsername(creatorUsername).orElseThrow();

        Group newGroup = Group.builder()
                .name(groupDto.getName())
                .creator(creator)
                .build();

        Set<MemberRole> roles = roleRepository.findByNameIn(List.of("Creador", "Descarga de Archivos", "Lectura de Eventos"));
        roles.forEach(System.out::println);
        Membership newMembership = Membership.builder()
                .group(newGroup)
                .user(creator)
                .roles(roles)
                .acceptedAt(LocalDateTime.now())
                .build();

        newGroup = groupRepository.save(newGroup);
        membershipRepository.save(newMembership);

        return Optional.of(GroupDto.fromEntity(newGroup));
    }

    @Override
    @Transactional
    public Optional<GroupDto> delete(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow( ()-> new AccessDeniedException("Access denied"));

        groupPolicy.authorize("delete", group);

        groupRepository.delete(group);

        return groupRepository.findById(groupId).map(GroupDto::fromEntity)
                .or(Optional::empty);
    }

    @Override
    @Transactional
    public Optional<UserDto> addMember(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId).orElseThrow( ()-> new AccessDeniedException("No se encontro el usuario con ID: " + userId));
        User newMember = userRepository.findById(userId).orElseThrow( ()-> new AccessDeniedException("No se encontro el grupo con ID: " + groupId));

        groupPolicy.authorize("addMembers", group);

        Optional<Membership> possibleMembership = membershipRepository.findByUserIdAndGroupId(userId, groupId);
        if(possibleMembership.isPresent() && possibleMembership.get().getAcceptedAt() != null){
            return Optional.empty();
        }

        Membership newMembership;
        newMembership = possibleMembership.orElseGet(() -> membershipRepository.save(
                Membership.builder()
                        .group(group)
                        .user(newMember)
                        .acceptedAt(null)
                        .roles(new HashSet<>())
                        .build()
                )
        );

        Optional<InvitationToken> token = invitationTokenService.refreshOrCreate(newMembership.getId());

        mailService.sendGroupInvitation(newMember, group, newMembership.getId(), token.orElseThrow(
                () -> new EmailException("No se encontro el token para la membresia: " + newMembership.getId())
        ));

        return Optional.of(UserDto.fromEntity(newMember).withCurrentRoles(newMembership));
    }

    @Override
    @Transactional
    public List<UserDto> addManyMembers(Long groupId, List<Long> userIds) {
        Group group = groupRepository.findById(groupId).orElseThrow( ()-> new AccessDeniedException("Access denied"));

        groupPolicy.authorize("addMembers", group);

        Set<MemberRole> basicRoles = roleRepository.findByNameIn(List.of("Descarga de Archivos", "Lectura de Eventos"));

        List<Membership> memberships = new ArrayList<>();
        List<User> users = userRepository.findAllByDeletedAtIsNullAndIdIn(userIds);

        users.forEach(user -> {
            Membership newMembership = Membership.builder()
                    .user(user)
                    .group(group)
                    .roles(basicRoles)
                    .build();

            memberships.add(newMembership);
        });

        List<Membership> membershipsDb = membershipRepository.saveAll(memberships);

        return membershipsDb.stream()
                .map(membership -> UserDto.fromEntity(membership.getUser()))
                .toList();
    }

    @Override
    @Transactional
    public Optional<MembershipDto> kickMember(Long groupId, Long memberId) {
        Group group = groupRepository.findById(groupId).orElseThrow( ()-> new AccessDeniedException("Access denied"));
        Membership membership = membershipRepository.findByUserIdAndGroupIdAndAcceptedAtNotNull(memberId, groupId).orElseThrow( ()-> new AccessDeniedException("Access denied"));

        groupPolicy.authorize("kickMembers", group, membership);

        membership.getRoles().clear();
        membershipRepository.delete(membership);

        return membershipRepository.findById(membership.getId()).map(MembershipDto::fromEntity)
                .or(Optional::empty);
    }

    @Override
    @Transactional
    public List<MemberRoleDto> addRole(Long groupId, Long memberId, Long roleId) {
        Group group = groupRepository.findById(groupId).orElseThrow( ()-> new AccessDeniedException("Access denied"));
        Membership membership = membershipRepository.findByUserIdAndGroupIdAndAcceptedAtNotNull(memberId, groupId).orElseThrow( ()-> new AccessDeniedException("Access denied"));

        groupPolicy.authorize("assignRoles", group, membership);

        Set<MemberRole> roles = membership.getRoles();
        roles.add(roleRepository.findById(roleId).orElseThrow());

        Set<MemberRole> persistedRoles = membershipRepository.save(membership).getRoles();

        return persistedRoles.stream()
                .map(MemberRoleDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public List<MemberRoleDto> deleteRole(Long groupId, Long memberId, Long roleId) {
        Group group = groupRepository.findById(groupId).orElseThrow( ()-> new AccessDeniedException("Access denied"));
        Membership membership = membershipRepository.findByUserIdAndGroupIdAndAcceptedAtNotNull(memberId, groupId).orElseThrow( ()-> new AccessDeniedException("Access denied"));

        groupPolicy.authorize("assignRoles", group, membership);

        membership.dropRole(roleRepository.findById(roleId).orElseThrow());
        Set<MemberRole> persistedRoles = membershipRepository.save(membership).getRoles();

        return persistedRoles.stream()
                .filter(memberRole -> Objects.equals(memberRole.getId(), roleId))
                .map(MemberRoleDto::fromEntity)
                .toList();
    }

    @Override
    public Map<String, Object> manageUsers(Long groupId) {
        Group group = groupRepository.findByIdWithMembersAndRoles(groupId).orElseThrow( ()-> new AccessDeniedException("Access denied"));

        groupPolicy.authorize("addMembers", group);

        List<UserDto> members = group.getMemberships()
                .stream()
                .filter(membership -> membership.getAcceptedAt() != null)
                .map(membership -> UserDto.fromEntity(membership.getUser())
                        .withCurrentRoles(membership))
                .toList();

        List<UserDto> usersWithPendingInvitation = group.getMemberships()
                .stream()
                .filter(membership -> membership.getAcceptedAt() == null)
                .map(membership -> UserDto.fromEntity(membership.getUser()))
                .toList();

        List<Long> subscribedIds = members.stream()
                .map(UserDto::getId)
                .toList();

        List<UserDto> unsubscribedUsers = userRepository.findAllByDeletedAtIsNullAndIdNotIn(subscribedIds)
                .stream()
                .map(UserDto::fromEntity)
                .toList();

        return Map.of(
                "success", true,
                "members", members,
                "usersWithPendingInvitation", usersWithPendingInvitation,
                "unsubscribedUsers", unsubscribedUsers
        );
    }

    @Override
    public Optional<Map<String,Object>> acceptMembership(Long membershipId, String invitationToken){
        boolean tokenWasRedeemed = invitationTokenService.attemptRedeem(membershipId, invitationToken);
        if(!tokenWasRedeemed){
            return Optional.empty();
        }

        Membership membership = membershipRepository.findById(membershipId).orElseThrow();

        List<String> roles;
        //Lógica para manejar roles de cuando se agrega al grupo público
        if(membership.getGroup().getId() != 1){
            roles = List.of("Descarga de Archivos", "Lectura de Eventos");
        }else{
            roles = List.of("Descarga de Archivos", "Lectura de Eventos", "Editor General de Eventos");
        }

        membership.setAcceptedAt(LocalDateTime.now());
        membership.setRoles(roleRepository.findByNameIn(roles));
        membership = membershipRepository.save(membership);

        Map<String, Object> response = Map.of(
                "user", UserDto.fromEntity(membership.getUser()),
                "group", GroupDto.fromEntity(membership.getGroup())
        );

        return Optional.of(response);
    }
}
