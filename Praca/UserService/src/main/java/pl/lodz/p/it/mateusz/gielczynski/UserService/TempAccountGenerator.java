package pl.lodz.p.it.mateusz.gielczynski.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;

//todo remove
@Configuration
@RequiredArgsConstructor
public class TempAccountGenerator {

    private final AccountRepository accountRepository;
    private final GroupRepository groupRepository;

//    @PostConstruct
//    @Transactional
//    public void generateAccounts() {
//        Account account = new Account();
//        account.setLanguage("pl");
//        account.setFirstName("Mateusz");
//        account.setLogin("zbych");
//        account.setPasswordHash("ZAQ!2wsx");
//        account.setLastName("Mateusz");
//        account.setEmail("cos@cos.pl");
//        Group zbychGroup = new Group("zbych");
//        zbychGroup.getRoles().add(new Role("Zbych", 1));
//        account.getGroups().add(zbychGroup);
//        accountRepository.saveAndFlush(account);
//        groupRepository.saveAndFlush(zbychGroup);
//    }

}
