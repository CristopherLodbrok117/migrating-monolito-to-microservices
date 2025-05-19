package app.calendar_service.application.repositories;

import app.calendar_service.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByGroupId(Long groupId);
    Optional<Category> findOneByNameAndGroupId(String name, Long groupId);
}
