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
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
class StartupService {

    private final AccountRepository accountRepository;
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void generateAccounts() {
        Role accountsRead = new Role("accounts:read", 1);
        Role accountsArchive = new Role("accounts:archive", 1);
        Role accountsInvite = new Role("accounts:invite", 1);
        Role groupsRead = new Role("groups:read", 1);
        Role groupsCreate = new Role("groups:create", 1);
        Role groupsDelete = new Role("groups:delete", 1);
        Role groupsEdit = new Role("groups:edit", 1);
        Role roleCreate = new Role("role:create", 1);
        Role roleDelete = new Role("role:delete", 1);
        Role roleRead = new Role("role:read", 1);
        Role votingsReadAll = new Role("votings:readAll", 1);
        Role votingsRead = new Role("votings:read", 1);
        Role votingsParticipate = new Role("votings:participate", 1);
        Role votingsManage = new Role("votings:manage", 1);
        Role estateRead = new Role("estate:read", 1);
        Role estateManage = new Role("estate:manage", 1);
        Role notificationRead = new Role("notifications:read", 1);
        Role notificationWrite = new Role("notifications:create", 1);
        Role notificationArchive = new Role("notifications:archive", 1);


        roleRepository.saveAndFlush(accountsRead);
        roleRepository.saveAndFlush(accountsArchive);
        roleRepository.saveAndFlush(accountsInvite);
        roleRepository.saveAndFlush(groupsRead);
        roleRepository.saveAndFlush(groupsCreate);
        roleRepository.saveAndFlush(groupsDelete);
        roleRepository.saveAndFlush(groupsEdit);
        roleRepository.saveAndFlush(roleCreate);
        roleRepository.saveAndFlush(roleDelete);
        roleRepository.saveAndFlush(roleRead);
        roleRepository.saveAndFlush(votingsReadAll);
        roleRepository.saveAndFlush(votingsRead);
        roleRepository.saveAndFlush(votingsParticipate);
        roleRepository.saveAndFlush(votingsManage);
        roleRepository.saveAndFlush(estateRead);
        roleRepository.saveAndFlush(estateManage);
        roleRepository.saveAndFlush(notificationRead);
        roleRepository.saveAndFlush(notificationWrite);
        roleRepository.saveAndFlush(notificationArchive);

        Group accManager = new Group("Accounts Manager", new HashSet<>(Set.of(accountsRead, accountsInvite, accountsArchive, groupsRead, groupsCreate, groupsDelete, groupsEdit, roleCreate, roleDelete, roleRead)), 1);
        Group votingCreator = new Group("Voting Creator", new HashSet<>(Set.of(votingsReadAll, votingsManage, accountsRead, estateRead, votingsRead)), 1);
        Group votingParticipant = new Group("Voting participant", new HashSet<>(Set.of(votingsParticipate, votingsRead, votingsReadAll)), 1);
        Group estateManager = new Group("Estate Manager", new HashSet<>(Set.of(estateRead, estateManage, accountsRead)), 1);
        Group notificationsUser = new Group("Notifications Manager", new HashSet<>(Set.of(notificationArchive, notificationRead, notificationWrite)), 1);


        groupRepository.saveAndFlush(accManager);
        groupRepository.saveAndFlush(votingCreator);
        groupRepository.saveAndFlush(votingParticipant);
        groupRepository.saveAndFlush(estateManager);
        groupRepository.saveAndFlush(notificationsUser);


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
        account.getGroups().add(accManager);
        account.getGroups().add(notificationsUser);
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
