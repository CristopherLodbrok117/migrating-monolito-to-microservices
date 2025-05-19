package app.calendar_service.web.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import app.calendar_service.domain.entities.Event;
import app.calendar_service.domain.entities.Group;
import app.calendar_service.domain.entities.Membership;
import app.calendar_service.domain.entities.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class GroupDto {
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ToString.Exclude
    private UserDto creator;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ToString.Exclude
    private Long creatorId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ToString.Exclude
    private Set<UserDto> members;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ToString.Exclude
    private Set<EventDto> events;

    public static GroupDto fromEntity(Group group) {
        return builder()
                .id(group.getId())
                .name(group.getName())
                .creatorId(group.getCreatorId())
                .build();
    }

    public GroupDto withEventsAndActivities(Collection<Event> events) {
        this.events = events.stream()
                .map(event -> EventDto.fromEntity(event).withActivities(event.getActivities()))
                .collect(Collectors.toSet());

        return this;
    }

    public GroupDto withMembers(Set<User> members) {
        this.members = members.stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toSet());

        return this;
    }

    public GroupDto withMembersFromMemberships(Set<Membership> memberships) {
        this.members = memberships.stream()
                .map(membership -> UserDto.fromEntity(membership.getUser())
                        .withCurrentRoles(membership))
                .collect(Collectors.toSet());

        return this;
    }
}
