package app.calendar_service.application.repositories;

import app.calendar_service.domain.entities.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public interface UserRepository extends JpaRepository<User, Long> {

    //List<User> findAllFromGroup(Long groupId);

    boolean existsOneByUsername(String username);
    boolean existsOneByEmail(String Email);

    Optional<User> findOneByUsername(String username);

    @Query("select u from User u left join fetch u.authorities where u.username = :username")
    Optional<User> findOneByUsernameWithAuthorities(@Param("username") String username);

    @Query("select u from User u left join fetch u.authorities where u.email = :email")
    Optional<User> findOneByEmailWithAuthorities(@Param("email") String email);

    @Query("select u from User u where u.username = :username")
    @EntityGraph(attributePaths = {"authorities", "memberships", "memberships.group"})
    Optional<User> findOneByUsernameWithDetails(@Param("username") String username);

    Optional<User> findOneByEmail(String Email);

    Optional<User> findOneByIdAndDeletedAtIsNull(long id);

    List<User> findAllByDeletedAtIsNull();

    List<User> findAllByDeletedAtIsNullAndIdIn(List<Long> ids);

    List<User> findAllByDeletedAtIsNullAndIdNotIn(List<Long> ids);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.memberships m LEFT JOIN FETCH m.group")
    List<User> findAllUsersWithMembershipsAndGroups();

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.memberships m LEFT JOIN FETCH m.group WHERE u.id = :id")
    List<User> findUserWithMembershipsAndGroups(@Param("id") Long id);


}
