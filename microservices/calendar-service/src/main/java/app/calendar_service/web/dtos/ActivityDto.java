package app.calendar_service.web.dtos;

import lombok.Builder;
import lombok.Data;
import app.calendar_service.domain.entities.Activity;
import app.calendar_service.domain.enums.ActivityStatus;

@Data
@Builder
public class ActivityDto {

    private Long id;
    private String name;
    private ActivityStatus status;
    private Long eventId;

    public static ActivityDto fromEntity(Activity activity){
        return ActivityDto.builder()
                .id(activity.getId())
                .name(activity.getName())
                .status(activity.getStatus())
                .eventId(activity.getEvent().getId())
                .build();

    }
}
