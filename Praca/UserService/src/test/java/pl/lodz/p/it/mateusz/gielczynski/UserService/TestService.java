package pl.lodz.p.it.mateusz.gielczynski.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.InviteCodeRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.PasswordResetTokenRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TestService {

    private final GroupRepository groupRepository;
    private final AccountRepository accountRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final InviteCodeRepository inviteCodeRepository;


    public Account createAccount(Account account) {
        return accountRepository.saveAndFlush(account);
    }

    public Group createGroup(Group group) {
        return groupRepository.saveAndFlush(group);
    }

    public Group getGroupById(UUID id) {
        return groupRepository.getGroupById(id).orElse(null);
    }

    public List<Account> findAllByGroups_Id(UUID groupId) {
        return accountRepository.findAllByGroups_Id(groupId);
    }


    @Transactional
    public void deleteAll() {
        passwordResetTokenRepository.deleteAll();
        accountRepository.deleteAll();
        groupRepository.deleteAll();
        inviteCodeRepository.deleteAll();
    }
}
