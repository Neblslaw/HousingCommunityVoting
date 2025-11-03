package pl.lodz.p.it.mateusz.gielczynski.UserService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Role;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;

//todo remove
@Configuration
@RequiredArgsConstructor
public class TempAccountGenerator {

    private final AccountRepository accountRepository;
    private final GroupRepository groupRepository;

    @PostConstruct
    public void generateAccounts() {
        Account account = new Account();
        account.setLanguage("pl");
        account.setFirstName("Mateusz");
        account.setLogin("zbych");
        account.setPasswordHash("ZAQ!2wsx");
        account.setLastName("Mateusz");
        account.setEmail("cos@cos.pl");
        Group zbychGroup = new Group("zbych");
        zbychGroup.getRoles().add(new Role("Zbych"));
        account.getGroups().add(zbychGroup);
        accountRepository.saveAndFlush(account);
        zbychGroup.getAccounts().add(account);
        groupRepository.saveAndFlush(zbychGroup);
    }

}
