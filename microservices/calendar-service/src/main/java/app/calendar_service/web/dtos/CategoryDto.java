package app.calendar_service.web.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import app.calendar_service.domain.entities.Category;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Min(value = 1)
    private Long groupId;

    public static CategoryDto fromEntity(Category category){
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .groupId(category.getGroupId())
                .build();
    }
}
