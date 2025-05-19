package app.calendar_service.application.services;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import app.calendar_service.application.repositories.InvitationTokenRepository;
import app.calendar_service.application.usecases.InvitationTokenService;
import app.calendar_service.domain.classes.TokenGenerator;
import app.calendar_service.domain.entities.InvitationToken;
import app.calendar_service.domain.entities.Membership;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvitationTokenServiceImpl implements InvitationTokenService {
    private final InvitationTokenRepository invitationTokenRepository;
    private final EntityManager entityManager;
    private final TokenGenerator tokenGenerator;

    @Override
    @Transactional
    public Optional<InvitationToken> refreshOrCreate(Long membershipId) {
        return invitationTokenRepository.findByMembershipId(membershipId)
                .map(token -> {
                    token.setToken(tokenGenerator.generateAlphaNumeric());
                    token.setCreatedAt(LocalDateTime.now());
                    return invitationTokenRepository.save(token);
                })
                .or(() -> Optional.of(invitationTokenRepository.save(
                        InvitationToken.builder()
                                .token(tokenGenerator.generateAlphaNumeric())
                                .membership(entityManager.getReference(Membership.class, membershipId))
                                .build()
                        ))
                );
    }

    @Override
    @Transactional
    public boolean attemptRedeem(Long membershipId, String token) {
        Optional<InvitationToken> foundToken = invitationTokenRepository
                .findByMembershipIdAndTokenAndCreatedAtGreaterThanEqual(membershipId, token, LocalDateTime.now().minusDays(7));

        if (foundToken.isEmpty()){
            return false;
        }

        invitationTokenRepository.delete(foundToken.get());

        return !invitationTokenRepository.existsById(foundToken.get().getId());
    }
}
