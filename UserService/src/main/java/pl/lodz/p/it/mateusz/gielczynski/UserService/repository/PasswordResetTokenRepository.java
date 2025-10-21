package pl.lodz.p.it.mateusz.gielczynski.UserService.repository;

import org.springframework.data.repository.Repository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.PasswordResetToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


public interface PasswordResetTokenRepository extends Repository<PasswordResetToken, UUID> {
    void saveAndFlush(PasswordResetToken passwordResetToken);

    Optional<PasswordResetToken> findById(UUID code);

    void deletePasswordResetTokenById(UUID code);

    Optional<PasswordResetToken> findByIdAndExpiresAtAfter(UUID code, LocalDateTime now);

    //todo fix pls
    void deleteAll();
}
