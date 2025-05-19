package app.calendar_service.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "invitation_tokens")
public class InvitationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne(targetEntity = Membership.class)
    private Membership membership;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
}
