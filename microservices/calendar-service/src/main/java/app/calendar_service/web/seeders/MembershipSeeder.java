package app.calendar_service.web.seeders;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.repositories.GroupRepository;
import app.calendar_service.application.repositories.MemberRoleRepository;
import app.calendar_service.application.repositories.MembershipRepository;
import app.calendar_service.application.repositories.UserRepository;
import app.calendar_service.domain.entities.Group;
import app.calendar_service.domain.entities.MemberRole;
import app.calendar_service.domain.entities.Membership;
import app.calendar_service.domain.entities.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MembershipSeeder implements Seeder{
    private final MembershipRepository membershipRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void seed() {
        List<User> users = userRepository.findAll();
        Set<MemberRole> roles = memberRoleRepository.findByNameIn("Lectura de Eventos", "Descarga de Archivos");

        List<Group> groups = groupRepository.findAll();

        groups.forEach(group -> {
            group.setMembers(users.subList(2,7));
            group.getMemberships().forEach( membership -> {
                Set<MemberRole> memberRoles = membership.getRoles();
                if (memberRoles != null){
                    memberRoles.addAll(roles);
                }else {
                    membership.setRoles(roles);
                }
            });
            membershipRepository.saveAll(group.getMemberships());
        });
    }

    @Override
    public String getName() {
        return "MembershipSeeder";
    }

    @Override
    public Integer getOrder() {
        return 4;
    }
}
