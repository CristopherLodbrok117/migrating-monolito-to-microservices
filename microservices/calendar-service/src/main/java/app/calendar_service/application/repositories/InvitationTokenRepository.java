package app.calendar_service.application.repositories;

import app.calendar_service.domain.entities.InvitationToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface InvitationTokenRepository extends CrudRepository<InvitationToken, Long> {
    Optional<InvitationToken> findByMembershipIdAndTokenAndCreatedAtGreaterThanEqual(Long membershipId, String token, LocalDateTime sevenDaysAgo);

    Optional<InvitationToken> findByMembershipId(Long membershipId);
}
