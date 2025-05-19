package app.calendar_service.web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import app.calendar_service.domain.entities.Membership;

@Data
@Builder

public class MembershipDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long groupId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long userId;

    public static MembershipDto fromEntity(Membership membership) {
        return builder().id(membership.getId())
                .groupId(membership.getGroup().getId())
                .userId(membership.getUser().getId())
                .build();
    }
}
