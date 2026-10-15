package pl.lodz.p.it.mateusz.gielczynski.UserService.integration.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.*;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.*;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TestDbHelper {

    private final AccountRepository accountRepository;
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final InviteCodeRepository inviteCodeRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public Optional<Account> findAccount(UUID id) {
        Optional<Account> account = accountRepository.findAccountById(id);
        account.ifPresent(a -> a.getGroups().size());
        return account;
    }

    public Optional<Account> findAccountByLogin(String login) {
        Optional<Account> account = accountRepository.findAccountByLogin(login);
        account.ifPresent(a -> a.getGroups().size());
        return account;
    }

    public Group saveGroup(Group group) {
        return groupRepository.saveAndFlush(group);
    }

    public Optional<Group> findGroup(UUID id) {
        Optional<Group> group = groupRepository.getGroupById(id);
        group.ifPresent(g -> g.getRoles().size());
        return group;
    }

    public Optional<Group> findGroupByName(String name) {
        Optional<Group> group = groupRepository.findGroupByName(name);
        group.ifPresent(g -> g.getRoles().size());
        return group;
    }

    public Account saveAccount(Account account) {
        return accountRepository.saveAndFlush(account);
    }

    public Role saveRole(Role role) {
        return roleRepository.saveAndFlush(role);
    }

    public Optional<Role> findRole(UUID id) {
        return roleRepository.findRoleById(id);
    }

    public Optional<Role> findRoleByName(String name) {
        return roleRepository.findAll().stream()
                .filter(r -> r.getName().equals(name))
                .findFirst();
    }

    public void saveInviteCode(InviteCode inviteCode) {
        inviteCodeRepository.saveAndFlush(inviteCode);
    }

    public void savePasswordResetToken(PasswordResetToken passwordResetToken) {
        passwordResetTokenRepository.saveAndFlush(passwordResetToken);
    }
}
