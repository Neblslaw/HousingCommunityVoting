package pl.lodz.p.it.mateusz.gielczynski.UserService.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class InviteCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    @Column(updatable = false)
    private UUID id;

    @Column()
    private LocalDateTime expiresAt;

    public InviteCode() {
        this.expiresAt = LocalDateTime.now().plusDays(1);
    }
}
