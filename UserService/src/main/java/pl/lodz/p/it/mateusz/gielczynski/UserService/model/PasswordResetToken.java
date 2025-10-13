package pl.lodz.p.it.mateusz.gielczynski.UserService.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    @Column(updatable = false)
    private UUID id;

    @Column()
    private LocalDateTime expiresAt;

    @OneToOne
    private Account account;

    public PasswordResetToken(Account account) {
        this.expiresAt = LocalDateTime.now().plusDays(1);
    }
}