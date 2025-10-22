package pl.lodz.p.it.mateusz.gielczynski.UserService.repository;

import org.springframework.data.repository.Repository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.InviteCode;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface InviteCodeRepository extends Repository<InviteCode, UUID> {

    void saveAndFlush(InviteCode inviteCode);

    boolean existsByIdAndExpiresAtAfter(UUID attr0, LocalDateTime expiresAtAfter);


    //todo fix pls
    void deleteAll();
}
