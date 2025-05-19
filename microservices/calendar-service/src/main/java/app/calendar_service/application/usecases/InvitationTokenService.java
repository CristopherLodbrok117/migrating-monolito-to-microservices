package app.calendar_service.application.usecases;

import app.calendar_service.domain.entities.InvitationToken;

import java.util.Optional;

public interface InvitationTokenService {
    Optional<InvitationToken> refreshOrCreate(Long membershipId);
    boolean attemptRedeem(Long membershipId, String token);
}
