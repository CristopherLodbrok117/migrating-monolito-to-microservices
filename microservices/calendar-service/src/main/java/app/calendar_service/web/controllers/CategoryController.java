package app.calendar_service.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import app.calendar_service.application.usecases.CategoryService;
import app.calendar_service.web.dtos.CategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups/{groupId}/categories")
@RequiredArgsConstructor
@PreAuthorize(value = "hasAnyRole('USER','SUPERUSER', 'ADMIN')")
@CrossOrigin(("http://localhost:5173"))
public class CategoryController {
    private final CategoryService categoryService;

    private ResponseEntity<?> responseWithErrors(BindingResult bindingResult){
        Map<String,String> errors =  new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping
    public ResponseEntity<?> allFromGroup(@PathVariable Long groupId) {
        List<CategoryDto> category =  categoryService.findAllByGroupId(groupId);

        return ResponseEntity.ok().body(category);
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable Long groupId, @Valid @RequestBody CategoryDto categoryDto, BindingResult result){
        if(result.hasErrors()){
            return this.responseWithErrors(result);
        }

        Optional<CategoryDto> created = categoryService.createCategory(groupId, categoryDto);

        if (created.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(created.get());
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> delete(@PathVariable Long groupId, @PathVariable Long categoryId){
        Optional<CategoryDto> deleted = categoryService.deleteCategory(groupId, categoryId);

        if (deleted.isPresent()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}
