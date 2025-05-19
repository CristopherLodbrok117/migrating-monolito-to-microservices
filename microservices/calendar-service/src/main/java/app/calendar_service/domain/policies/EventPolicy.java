package app.calendar_service.domain.policies;

import app.calendar_service.application.repositories.MembershipRepository;
import app.calendar_service.application.repositories.UserRepository;
import app.calendar_service.domain.entities.Event;
import app.calendar_service.domain.entities.Group;
import app.calendar_service.domain.entities.Membership;
import app.calendar_service.domain.entities.User;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class EventPolicy extends SinaloaPolicy{
    private final MembershipRepository membershipRepository;

    public EventPolicy(UserRepository userRepository, MembershipRepository membershipRepository) {
        super(userRepository);
        this.membershipRepository = membershipRepository;
    }

    public boolean view(User authUser, Group group) {
        Optional<Membership> membership = membershipRepository.findByUserIdAndGroupIdAndAcceptedAtNotNull(authUser.getId(), group.getId());

        return membership.isPresent()
                && hasAnyRole(Set.of("Creador", "Administrador", "Lectura de Eventos"), membership.get().getRoles());
    }

    public boolean create(User authUser, Group group) {
        Optional<Membership> membership = membershipRepository.findByUserIdAndGroupIdAndAcceptedAtNotNull(authUser.getId(), group.getId());

        return membership.isPresent()
                && hasAnyRole(Set.of("Creador", "Administrador", "Editor General de Eventos", "Escritura de Eventos"), membership.get().getRoles());
    }

    public boolean update(User authUser, Group group, Event event) {
        Optional<Membership> membership = membershipRepository.findByUserIdAndGroupIdAndAcceptedAtNotNull(authUser.getId(), group.getId());

        return membership.isPresent()
                && (Objects.equals(event.getCreator().getId(), authUser.getId())
                || hasAnyRole(Set.of("Creador", "Administrador", "Editor General de Eventos"), membership.get().getRoles()));
    }

    public boolean delete(User authUser, Group group, Event event) {
        Optional<Membership> membership = membershipRepository.findByUserIdAndGroupIdAndAcceptedAtNotNull(authUser.getId(), group.getId());

        return membership.isPresent()
                && (Objects.equals(event.getCreator().getId(), authUser.getId())
                || hasAnyRole(Set.of("Creador", "Administrador", "Editor General de Eventos"), membership.get().getRoles()));
    }
}
