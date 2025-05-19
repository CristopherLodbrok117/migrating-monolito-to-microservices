package app.calendar_service.application.usecases;

import app.calendar_service.web.dtos.CategoryDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDto> findAllByGroupId(Long groupId);
    Optional<CategoryDto> createCategory(Long groupId, CategoryDto categoryDto);
    Optional<CategoryDto> deleteCategory(Long groupId, Long categoryId);
}
