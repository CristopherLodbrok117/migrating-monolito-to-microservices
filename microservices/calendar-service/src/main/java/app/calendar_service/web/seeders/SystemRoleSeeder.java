package app.calendar_service.web.seeders;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import app.calendar_service.application.repositories.SystemRoleRepository;
import app.calendar_service.domain.entities.SystemRole;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SystemRoleSeeder implements Seeder{

    private final SystemRoleRepository systemRoleRepository;

    @Override
    @Transactional
    public void seed() {
        List<SystemRole> roles = new ArrayList<>();

        roles.add(new SystemRole("ROLE_SUPERUSER"));
        roles.add(new SystemRole("ROLE_ADMIN"));
        roles.add(new SystemRole("ROLE_USER"));

        systemRoleRepository.saveAll(roles);
    }

    @Override
    public String getName() {
        return "SystemRoleSeeder";
    }

    @Override
    public Integer getOrder() {
        return 0;
    }
}
