package app.calendar_service.web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import app.calendar_service.domain.entities.SystemRole;

@Data
@AllArgsConstructor
public class SystemRoleDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

    public static SystemRoleDto fromEntity(SystemRole systemRole){
        return new SystemRoleDto(
                systemRole.getId(),
                systemRole.getName()
        );
    }
}
