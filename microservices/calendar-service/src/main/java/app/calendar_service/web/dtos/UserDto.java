package app.calendar_service.web.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.ToString;
import app.calendar_service.domain.entities.Group;
import app.calendar_service.domain.entities.Membership;
import app.calendar_service.domain.entities.SystemRole;
import app.calendar_service.domain.entities.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserDto {

    @JsonProperty(value = "uid")
    private Long id;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    @NotBlank
    @Size(min = 8, max = 24)
    private String password;

    @NotBlank
    @Size(min = 8, max = 30)
    @Pattern(regexp = "^[a-zA-Z_][a-zA-Z0-9_]*$",
    message = "solo puede contener letras mayusculas y minusculas, numeros y el guión bajo")
    private String username;

    @PastOrPresent
    private LocalDateTime created_at;

    @PastOrPresent
    private LocalDateTime updated_at;

    @ToString.Exclude
    @PastOrPresent
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDateTime deleted_at;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<SystemRoleDto> authorities;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<GroupDto> groups;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<MemberRoleDto> currentRoles;

    public UserDto(
            Long id,
            String email,
            String password,
            String username,
            LocalDateTime created_at,
            LocalDateTime updated_at,
            LocalDateTime deleted_at
    ){
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    public static UserDto fromEntity(User user){
        return user == null ? null : new UserDto(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt()
      );
    }

    public UserDto withAuthorities(Collection<SystemRole> authorities) {
        this.authorities = authorities.stream()
                .map(SystemRoleDto::fromEntity)
                .collect(Collectors.toSet());

        return this;
    }

    public UserDto withGroups(Collection<Group> groups) {
        this.groups = groups.stream()
                .map(GroupDto::fromEntity)
                .sorted(Comparator.comparing(GroupDto::getId))
                .toList();

        return this;
    }

    public UserDto withCurrentRoles(Membership membership) {
        this.currentRoles = membership.getRoles().stream()
                .map(MemberRoleDto::fromEntity)
                .sorted(Comparator.comparing(MemberRoleDto::getId))
                .toList();

        return this;
    }
}
