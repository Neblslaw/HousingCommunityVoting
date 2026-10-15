package pl.lodz.p.it.mateusz.gielczynski.UserService.repository;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface AccountRepository extends Repository<Account, UUID> {

    List<Account> findAll();

    Optional<Account> findAccountById(UUID id);

    Account saveAndFlush(Account account);

    Optional<Account> findAccountByLogin(String login);

    boolean existsAccountByEmailAndArchived(String email, boolean b);

    Optional<Account> findAccountByEmail(String email);

    boolean existsAccountByEmail(String email);

    boolean existsAccountByLogin(String login);

    //todo fix pls
    void deleteAll();

    List<Account> findAllByGroups_Id(UUID groups_id);
}
