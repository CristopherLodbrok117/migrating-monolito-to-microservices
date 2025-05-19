package app.calendar_service.web.seeders;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.repositories.SystemRoleRepository;
import app.calendar_service.application.repositories.UserRepository;
import app.calendar_service.domain.entities.SystemRole;
import app.calendar_service.domain.entities.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class UserSeeder implements Seeder{

    private final UserRepository userRepository;
    private final SystemRoleRepository systemRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void seed() {
        Map<String,SystemRole> roles = systemRoleRepository.findAll().stream()
                .collect(Collectors.toMap(SystemRole::getName, Function.identity()));

        Set<SystemRole> userRole = new HashSet<>();
        userRole.add(roles.get("ROLE_USER"));

        List<User> seedUsers = List.of(
                User.builder()
                        .username("SinaloaAdmin")
                        .password(passwordEncoder.encode("secreto1"))
                        .email("arribamana@gmail.com")
                        .authorities(
                                new HashSet<>(roles.values())
                        )
                        .build(),

                User.builder()
                        .username("manolin92")
                        .password(passwordEncoder.encode("secreto1"))
                        .email("manolin@gmail.com")
                        .authorities(
                                new HashSet<>(userRole)
                        )
                        .build(),

                User.builder()
                        .username("chilinsk4i")
                        .password(passwordEncoder.encode("secreto1"))
                        .email("chilinski@gmail.com")
                        .authorities(
                                new HashSet<>(userRole)
                        )
                        .build(),

                User.builder()
                        .username("rosameltrozo")
                        .password(passwordEncoder.encode("webos1987"))
                        .email("rosalamanguera@outlook.com")
                        .authorities(
                                new HashSet<>(userRole)
                        )
                        .build(),

                User.builder()
                        .username("romuloYremo")
                        .password(passwordEncoder.encode("secreto1"))
                        .email("romasigloi@gmail.com")
                        .authorities(
                                new HashSet<>(userRole)
                        )
                        .build(),

                User.builder()
                        .username("maclovin12")
                        .password(passwordEncoder.encode("secreto1"))
                        .email("maclovin12@gmail.com")
                        .authorities(
                                new HashSet<>(userRole)
                        )
                        .build(),

                User.builder()
                        .username("kakaroto23")
                        .password(passwordEncoder.encode("secreto1"))
                        .email("kakaroto23@gmail.com")
                        .authorities(
                                new HashSet<>(userRole)
                        )
                        .build(),

                User.builder()
                        .username("heisenberg97")
                        .password(passwordEncoder.encode("secreto1"))
                        .email("heisenberg97@gmail.com")
                        .authorities(
                                new HashSet<>(userRole)
                        )
                        .build(),

                User.builder()
                        .username("fabianruelas1")
                        .password(passwordEncoder.encode("secreto1"))
                        .email("fabianruelas1@gmail.com")
                        .authorities(
                                new HashSet<>(userRole)
                        )
                        .build(),

                User.builder()
                        .username("rodolfocardenas")
                        .password(passwordEncoder.encode("secreto1"))
                        .email("rodolfocardenas@gmail.com")
                        .authorities(
                                new HashSet<>(userRole)
                        )
                        .build()
        );

        userRepository.saveAll(new ArrayList<>(seedUsers));
    }


    @Override
    public String getName() {
        return "UserSeeder";
    }

    @Override
    public Integer getOrder() {
        return 1;
    }
}
