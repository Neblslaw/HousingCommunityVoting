package pl.lodz.p.it.mateusz.gielczynski.UserService.integration.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TestDbHelper {

    private final AccountRepository accountRepository;
    private final GroupRepository groupRepository;

    public Optional<Account> findAccount(UUID id) {
        Optional<Account> account = accountRepository.findAccountById(id);
        account.ifPresent(a -> a.getGroups().size());
        return account;
    }

    public Group saveGroup(Group group) {
        return groupRepository.saveAndFlush(group);
    }

    public Account saveAccount(Account account) {
        return accountRepository.saveAndFlush(account);
    }
}
