package pl.lodz.p.it.mateusz.gielczynski.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Role;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;

@Service
@RequiredArgsConstructor
class StartupService {

    private final AccountRepository accountRepository;
    private final GroupRepository groupRepository;

    @Transactional
    public void generateAccounts() {
        Account account = new Account();
        account.setLanguage("pl");
        account.setFirstName("Mateusz");
        account.setLogin("zbych");
        account.setPasswordHash("ZAQ!2wsx");
        account.setLastName("Mateusz");
        account.setEmail("cos@cos.pl");
        Group zbychGroup = new Group("zbych");
        zbychGroup.getRoles().add(new Role("Zbych", 1));
        account.getGroups().add(zbychGroup);
        accountRepository.saveAndFlush(account);
        groupRepository.saveAndFlush(zbychGroup);
    }

}
