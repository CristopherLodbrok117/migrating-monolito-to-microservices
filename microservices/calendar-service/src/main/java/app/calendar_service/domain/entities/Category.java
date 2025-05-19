package app.calendar_service.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "event_categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "group_id", updatable = false, insertable = false)
    private Long groupId;

    @ManyToOne(targetEntity = Group.class)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;
}
