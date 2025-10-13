package pl.lodz.p.it.mateusz.gielczynski.UserService.repository;

import org.springframework.data.repository.Repository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends Repository<Account, UUID> {

    List<Account> getAccounts();

    Optional<Account> getAccountById(UUID id);

    void saveAndFlush(Account account);

    Account getAccountByLogin(String login);

    boolean existsAccountByEmailAndArchived(String email, boolean b);

    Optional<Account> getAccountByEmail(String email);

    boolean existsAccountByEmail(String email);

    boolean existsAccountByLogin(String login);
}
