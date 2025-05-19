package app.calendar_service.application.repositories;

import app.calendar_service.domain.entities.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {
    Set<MemberRole> findByNameIn(List<String> names);
    default Set<MemberRole> findByNameIn(String ...names){
        return findByNameIn(List.of(names));
    }

    Optional<MemberRole> findByName(String name);
}
