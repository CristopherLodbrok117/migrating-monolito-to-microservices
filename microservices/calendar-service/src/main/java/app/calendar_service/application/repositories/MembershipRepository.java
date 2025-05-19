package app.calendar_service.application.repositories;

import app.calendar_service.domain.entities.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership,Long> {

    boolean existsByUserIdAndGroupId(Long userId, Long groupId);
    Optional<Membership> findByUserIdAndGroupIdAndAcceptedAtNotNull(Long userId, Long groupId);
    Optional<Membership> findByUserIdAndGroupId(Long userId, Long groupId);
    Optional<Membership> findByUser_Username(String username);
    List<Membership> findAllByGroup_Creator_IdIn(List<Long> ids);
}
