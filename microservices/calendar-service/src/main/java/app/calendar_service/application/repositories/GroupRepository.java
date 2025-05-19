package app.calendar_service.application.repositories;

import app.calendar_service.domain.entities.Group;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {
    @Query("select g from Group g")
    @EntityGraph(attributePaths = {"memberships", "memberships", "memberships.roles"})
    List<Group> findAllWithMembers();

    @Query("select g from Group g where g.id = :id")
    @EntityGraph(attributePaths = {"memberships", "memberships.user", "memberships.roles", "events", "events.activities"})
    Optional<Group> findByIdWithDetails(@Param("id") Long id);

    @Query("select g from Group g where g.id = :id")
    @EntityGraph(attributePaths = {"memberships", "memberships.user", "categories"})
    Optional<Group> findByIdUseCase(@Param("id") Long id);

    @Query("select g from Group g where g.id = :id")
    @EntityGraph(attributePaths = {"memberships", "memberships.user", "memberships.roles"})
    Optional<Group> findByIdWithMembersAndRoles(@Param("id") Long id);

    @Query("select g from Group g where g.id = :groupId")
    @EntityGraph(attributePaths = {"memberships.user", "categories", "files"})
    Optional<Group> fetchEventFormResourcesByGroup(@Param("groupId") Long groupId);
}
