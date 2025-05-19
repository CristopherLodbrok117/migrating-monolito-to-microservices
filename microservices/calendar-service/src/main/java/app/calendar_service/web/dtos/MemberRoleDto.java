package app.calendar_service.web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import app.calendar_service.domain.entities.MemberRole;

@Data
@AllArgsConstructor

public class MemberRoleDto {
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

    public static MemberRoleDto fromEntity(MemberRole memberRole) {
        return new MemberRoleDto(memberRole.getId(), memberRole.getName());
    }
}
