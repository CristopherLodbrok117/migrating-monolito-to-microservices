package app.calendar_service.web.seeders;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.repositories.MemberRoleRepository;
import app.calendar_service.domain.entities.MemberRole;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberRoleSeeder implements Seeder{
    private final MemberRoleRepository roleRepository;

    @Override
    public void seed() {
        List<MemberRole> roleList = new ArrayList<>();

        roleList.add(MemberRole.builder().name("Creador").build());
        roleList.add(MemberRole.builder().name("Administrador").build());
        roleList.add(MemberRole.builder().name("Agregar Miembros").build());
        roleList.add(MemberRole.builder().name("Lectura de Eventos").build());
        roleList.add(MemberRole.builder().name("Escritura de Eventos").build());
        roleList.add(MemberRole.builder().name("Editor General de Eventos").build());
        roleList.add(MemberRole.builder().name("Descarga de Archivos").build());
        roleList.add(MemberRole.builder().name("Subida de Archivos").build());

        roleRepository.saveAll(roleList);
    }

    @Override
    public String getName() {
        return "MemberRoleSeeder";
    }

    @Override
    public Integer getOrder() {
        return 2;
    }
}
