package app.calendar_service.web.seeders;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.repositories.*;
import app.calendar_service.domain.entities.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GroupSeeder implements Seeder{

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final MemberRoleRepository roleRepository;
    private final MemberRoleRepository memberRoleRepository;

    @Override
    @Transactional
    public void seed() {
        List<Group> groups = new ArrayList<>();

        groups.add(
                Group.builder()
                        .name("Public Group")
                        .creator(userRepository.findOneByUsername("SinaloaAdmin").orElseThrow())
                        .build()
        );

        groups.add(
                Group.builder()
                        .name("Grupo Privado")
                        .creator(userRepository.findOneByUsername("SinaloaAdmin").orElseThrow())
                        .build()
        );

        groups.add(
                Group.builder()
                        .name("Grupo Privado Manolin")
                        .creator(userRepository.findOneByUsername("manolin92").orElseThrow())
                        .build()
        );

        groupRepository.saveAll(groups);

        Group publicGroup = groupRepository.findById(1L).orElseThrow();
        Group privateGroup = groupRepository.findById(2L).orElseThrow();
        Group manolinGroup = groupRepository.findById(3L).orElseThrow();

        publicGroup.setMembers(List.of(userRepository.findOneByUsername("SinaloaAdmin").orElseThrow()));
        privateGroup.setMembers(List.of(userRepository.findOneByUsername("SinaloaAdmin").orElseThrow()));
        manolinGroup.setMembers(List.of(userRepository.findOneByUsername("manolin92").orElseThrow()));

        Set<MemberRole> roles = roleRepository.findByNameIn("Creador");
        publicGroup.getMemberships().forEach(membership -> membership.setRoles(roles));
        privateGroup.getMemberships().forEach(membership -> membership.setRoles(roles));
        manolinGroup.getMemberships().forEach(membership -> membership.setRoles(roles));

        membershipRepository.saveAll(publicGroup.getMemberships());
        membershipRepository.saveAll(privateGroup.getMemberships());
        membershipRepository.saveAll(manolinGroup.getMemberships());
    }

    @Override
    public String getName() {
        return "GroupSeeder";
    }

    @Override
    public Integer getOrder() {
        return 3;
    }
}
