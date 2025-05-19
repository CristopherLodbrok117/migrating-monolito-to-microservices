package app.calendar_service.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import app.calendar_service.domain.enums.EventPriority;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String notes;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Enumerated(EnumType.ORDINAL)
    private EventPriority priority;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(targetEntity = Activity.class, fetch = FetchType.LAZY, mappedBy = "event")
    private List<Activity> activities;

    @Column(name = "creator_id", updatable = false, insertable = false)
    private Long creatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    @ToString.Exclude
    private User creator;

    @Column(name = "group_id", updatable = false, insertable = false)
    private Long groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @ToString.Exclude
    private Group group;

    @Column(name = "category_id", updatable = false, insertable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ToString.Exclude
    private Category category;

    @Column(name = "assignee_id", updatable = false, insertable = false)
    private Long assigneeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id", referencedColumnName = "id")
    @ToString.Exclude
    private User assignee;

    @Transient
    private Set<FileMetadata> files;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

//    public void addFile(FileMetadata fileMetadata){
//        if (this.files == null){
//            files = new HashSet<>();
//        }
//
//        files.add(fileMetadata);
//    }
}
