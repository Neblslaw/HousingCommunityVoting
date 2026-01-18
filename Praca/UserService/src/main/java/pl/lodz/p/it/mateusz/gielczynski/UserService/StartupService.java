package pl.lodz.p.it.mateusz.gielczynski.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void generateAccounts() {
        Account account = new Account();
        account.setLanguage("pl");
        account.setFirstName("Mateusz");
        account.setLogin("zbych");
        account.setPasswordHash(passwordEncoder.encode("ZAQ!2wsx"));
        account.setLastName("Mateusz");
        account.setEmail("cos@cos.pl");
        Group zbychGroup = new Group("zbych");
        zbychGroup.getRoles().add(new Role("Zbych", 1));
        account.getGroups().add(zbychGroup);
        accountRepository.saveAndFlush(account);
        groupRepository.saveAndFlush(zbychGroup);

        Account account2 = new Account();
        account2.setLanguage("pl");
        account2.setFirstName("Mateusz");
        account2.setLogin("zbych2");
        account2.setPasswordHash(passwordEncoder.encode("ZAQ!2wsx"));
        account2.setLastName("Matesusz");
        account2.setEmail("cos@coss.pl");
        Group zbychGroup2 = new Group("zbych2");
        zbychGroup2.getRoles().add(new Role("Zbych2", 1));
        account2.getGroups().add(zbychGroup2);
        accountRepository.saveAndFlush(account2);
        groupRepository.saveAndFlush(zbychGroup2);
    }

}
