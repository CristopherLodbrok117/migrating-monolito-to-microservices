package app.calendar_service.web.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionsDto {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean view;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean updateName;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean delete;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean assignRoles;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean addMembers;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean kickMembers;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean kickAnyMember;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean manageCategories;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean downloadFile;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean manageFiles;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean viewEvents;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean createEvents;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean updateEvent;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean deleteEvent;
}
