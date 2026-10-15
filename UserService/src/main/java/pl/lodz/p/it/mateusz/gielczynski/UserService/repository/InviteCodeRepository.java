package pl.lodz.p.it.mateusz.gielczynski.UserService.repository;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.InviteCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface InviteCodeRepository extends Repository<InviteCode, UUID> {

    void saveAndFlush(InviteCode inviteCode);

    boolean existsByIdAndExpiresAtAfter(UUID attr0, LocalDateTime expiresAtAfter);


    //todo fix pls
    void deleteAll();
}
