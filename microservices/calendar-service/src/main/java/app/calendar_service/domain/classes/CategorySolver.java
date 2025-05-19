package app.calendar_service.domain.classes;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import app.calendar_service.application.repositories.CategoryRepository;
import app.calendar_service.domain.entities.Category;
import app.calendar_service.domain.entities.Group;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategorySolver {

    private final CategoryRepository categoryRepository;
    private final EntityManager entityManager;

    public Category manageCategory(String categoryName, Long groupId){
        if(categoryName== null || categoryName.isEmpty()){
            return null;
        }

        Optional<Category> categoryOptional = categoryRepository.findOneByNameAndGroupId(categoryName, groupId);

        return categoryOptional.or( () -> Optional.of(categoryRepository.save(
                Category.builder()
                        .name(categoryName)
                        .group(entityManager.getReference(Group.class, groupId))
                        .build()
                )))
                .orElseThrow();
    }
}
