package app.calendar_service.domain.policies;

import app.calendar_service.application.repositories.MembershipRepository;
import app.calendar_service.application.repositories.UserRepository;
import app.calendar_service.domain.entities.Group;
import app.calendar_service.domain.entities.Membership;
import app.calendar_service.domain.entities.User;
import app.calendar_service.web.dtos.GroupDto;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class GroupPolicy extends SinaloaPolicy{

    private final MembershipRepository membershipRepository;

    public GroupPolicy(UserRepository userRepository, MembershipRepository membershipRepository) {
        super(userRepository);
        this.membershipRepository = membershipRepository;
    }

    public boolean view(User authUser, GroupDto group) {
        return membershipRepository.existsByUserIdAndGroupId(authUser.getId(), group.getId());
    }

    public boolean updateName(User authUser, Group group) {
        return Objects.equals(group.getCreatorId(), authUser.getId());
    }

    public boolean delete(User authUser, Group group) {
        return Objects.equals(group.getCreatorId(), authUser.getId());
    }

    public boolean assignRoles(User authUser, Group group, Membership membership) {
        Optional<Membership> authMembership = membershipRepository.findByUserIdAndGroupIdAndAcceptedAtNotNull(authUser.getId(), group.getId());

        if(membership.hasRole("Creador")){
            return Objects.equals(group.getCreatorId(), authUser.getId());
        }

        return authMembership.isPresent()
                && hasAnyRole(Set.of("Creador", "Administrador"), authMembership.get().getRoles());
    }

    public boolean assignAnyRoles(User authUser, Group group) {
        Optional<Membership> authMembership = membershipRepository.findByUserIdAndGroupIdAndAcceptedAtNotNull(authUser.getId(), group.getId());

        return authMembership.isPresent()
                && hasAnyRole(Set.of("Creador", "Administrador"), authMembership.get().getRoles());
    }

    public boolean addMembers(User authUser, Group group) {
        Optional<Membership> authMembership = membershipRepository.findByUserIdAndGroupIdAndAcceptedAtNotNull(authUser.getId(), group.getId());

        return authMembership.isPresent()
                && hasAnyRole(Set.of("Creador", "Administrador", "Agregar Miembros"), authMembership.get().getRoles());
    }

    public boolean kickMembers(User authUser, Group group, Membership membership) {
        Optional<Membership> authMembership = membershipRepository.findByUserIdAndGroupIdAndAcceptedAtNotNull(authUser.getId(), group.getId());

        if(membership.hasRole("Creador")){
            return false;
        }

        return authMembership.isPresent()
                && hasAnyRole(Set.of("Creador", "Administrador", "Eliminar Miembros"), authMembership.get().getRoles());
    }

    public boolean kickAnyMember(User authUser, Group group) {
        Optional<Membership> authMembership = membershipRepository.findByUserIdAndGroupIdAndAcceptedAtNotNull(authUser.getId(), group.getId());

        return authMembership.isPresent()
                && hasAnyRole(Set.of("Creador", "Administrador", "Eliminar Miembros"), authMembership.get().getRoles());
    }

    public boolean manageCategories(User authUser, Group group) {
        Optional<Membership> authMembership = membershipRepository.findByUserIdAndGroupIdAndAcceptedAtNotNull(authUser.getId(), group.getId());

        return authMembership.isPresent()
                && hasAnyRole(Set.of("Creador", "Administrador"), authMembership.get().getRoles());
    }
}
