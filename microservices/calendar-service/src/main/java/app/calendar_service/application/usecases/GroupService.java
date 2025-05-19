package app.calendar_service.application.usecases;

import app.calendar_service.web.dtos.*;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GroupService {
    List<GroupDto> findAll();

    public Optional<Pair<GroupDto, UserPermissionsDto>> findOneWithDetails(Long id);

    Optional<GroupDto> update(Long groupId, GroupDto groupDto);

    Optional<GroupDto> save(String creatorUsername, GroupDto groupDto);

    Optional<GroupDto> delete(Long groupId);

    Optional<UserDto> addMember(Long groupId, Long userId);

    List<UserDto> addManyMembers(Long groupId, List<Long> userIds);

    Optional<MembershipDto> kickMember(Long groupId, Long memberId);

    List<MemberRoleDto> addRole(Long groupId, Long memberId, Long roleId);

    List<MemberRoleDto> deleteRole(Long groupId, Long memberId, Long roleIds);

    Map<String, Object> manageUsers(Long groupId);

    Optional<Map<String, Object>> acceptMembership(Long membershipId, String invitationToken);
}
