package app.calendar_service.web.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import app.calendar_service.domain.entities.*;
import app.calendar_service.domain.enums.EventPriority;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class EventDto {

    private Long id;

    private String title;
    private String notes;

    @JsonProperty(value = "start")
    private LocalDateTime startDate;

    @JsonProperty(value = "end")
    private LocalDateTime endDate;

    private EventPriority priority;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ActivityDto> activities;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private UserDto creator;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String category;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long creatorId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long groupId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long categoryId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long assigneeId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private UserDto assignee;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Long> fileIds;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FileMetadataDto> files;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private UserPermissionsDto userCan;

    public static EventDto fromEntity(Event event){
        return EventDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .notes(event.getNotes())
                .priority(event.getPriority())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }

    public EventDto withActivities(Collection<Activity> activities){
        this.activities = activities.stream()
                .map(ActivityDto::fromEntity)
                .toList();

        return this;
    }

    public EventDto withCreator(User user){
        this.creator = UserDto.fromEntity(user);
        return this;
    }

    public EventDto withCategory(Category category){
        if(category == null){
            return this;
        }

        this.category = category.getName();
        return this;
    }

    public EventDto withUserPermissions(UserPermissionsDto userPermissions){
        this.userCan = userPermissions;
        return this;
    }

    public EventDto withAssignee(User assignee){
        this.assignee = UserDto.fromEntity(assignee);
        return this;
    }

    public EventDto withFiles(Collection<FileMetadata> files){
        this.files = files.stream().map(FileMetadataDto::fromEntity).toList();
        return this;
    }
}
