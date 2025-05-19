package app.calendar_service.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    @ToString.Exclude
    @Size(min = 3)
    private String password;

    @NotBlank
    @Size(min = 8, max = 30)
    @Pattern(regexp = "^[a-zA-Z_][a-zA-Z0-9_]*$")
    private String username;

    @PastOrPresent
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PastOrPresent
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ToString.Exclude
    @PastOrPresent
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToMany
    @JoinTable(
            name ="users_system_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "system_role_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","system_role_id"})
    )
    private Set<SystemRole> authorities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Membership> memberships;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Group> groupsCreated;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return email == null ? this.hashCode() : Objects.hash(email);
    }

    public User withAuthority(SystemRole systemRole)
    {
        if(authorities == null){
            authorities = new HashSet<>();
        }

        this.authorities.add(systemRole);

        return this;
    }

    public User withMembershipToGroup(Group group)
    {
        if(memberships == null){
            memberships = new HashSet<>();
        }

        LocalDateTime acceptedAt = null;
        if (group.getId() == 1){
            acceptedAt = LocalDateTime.now();
        }

        this.memberships.add(
                Membership.builder()
                        .group(group)
                        .user(this)
                        .acceptedAt(acceptedAt)
                        .build()
        );

        return this;
    }

    public Set<Group> getGroups(){
        return memberships.stream()
                .map(Membership::getGroup)
                .collect(Collectors.toSet());
    }
}
