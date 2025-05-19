package app.calendar_service.application.services;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.repositories.CategoryRepository;
import app.calendar_service.application.repositories.GroupRepository;
import app.calendar_service.application.usecases.CategoryService;
import app.calendar_service.domain.entities.Category;
import app.calendar_service.domain.entities.Group;
import app.calendar_service.domain.policies.GroupPolicy;
import app.calendar_service.web.dtos.CategoryDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;

    private final GroupPolicy groupPolicy;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findAllByGroupId(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow();

        groupPolicy.authorize("manageCategories", group);

        List<Category> categories = categoryRepository.findAllByGroupId(groupId);

        return categories.stream()
                .map(CategoryDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public Optional<CategoryDto> createCategory(Long groupId, CategoryDto categoryDto) {
        Group group = groupRepository.findById(groupId).orElseThrow();

        groupPolicy.authorize("manageCategories", group);

        Category category = Category.builder()
                .name(categoryDto.getName())
                .group(group)
                .build();

        category = categoryRepository.save(category);

        return Optional.of(CategoryDto.fromEntity(category));
    }

    @Override
    @Transactional
    public Optional<CategoryDto> deleteCategory(Long groupId, Long categoryId) {
        Group group = groupRepository.findById(groupId).orElseThrow();

        groupPolicy.authorize("manageCategories", group);

        Category category = categoryRepository.findById(categoryId).orElseThrow();
        categoryRepository.delete(category);

        return categoryRepository.findById(categoryId)
                .map(CategoryDto::fromEntity)
                .or(Optional::empty);
    }
}
