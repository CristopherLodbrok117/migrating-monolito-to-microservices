package app.calendar_service.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString

@Entity
@Table(name = "system_roles")
public class SystemRole implements GrantedAuthority{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotNull
    @Column(name = "name", unique = true)
    private String name;

    public SystemRole(String name){
        this.name = name;
    }

    @ManyToMany(mappedBy = "authorities")
    @JsonIgnore
    @ToString.Exclude
    @JsonIgnoreProperties({"authorities","hibernateLazyInitializer", "handler"})
    private List<User> users = new ArrayList<>();

    public SystemRole(){
        this.users = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemRole role = (SystemRole) o;
        return Objects.equals(id, role.id) && Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
