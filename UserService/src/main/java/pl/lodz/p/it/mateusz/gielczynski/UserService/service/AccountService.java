package pl.lodz.p.it.mateusz.gielczynski.UserService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.mateusz.gielczynski.UserService.Exceptions.BadRequestException;
import pl.lodz.p.it.mateusz.gielczynski.UserService.Exceptions.NotFoundException;
import pl.lodz.p.it.mateusz.gielczynski.UserService.consts.i18n;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.email.EmailService;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.InviteCode;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.PasswordResetToken;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.InviteCodeRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.PasswordResetTokenRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final InviteCodeRepository inviteCodeRepository;
    private final GroupRepository groupRepository;

    public List<Account> getAccounts() {
        return accountRepository.getAccounts();
    }

    public Account getAccount(UUID id) throws  NotFoundException {
        return accountRepository.getAccountById(id).orElseThrow(() -> new NotFoundException(i18n.ACCOUNT_NOT_FOUND));
    }

    public void updateAccount(String login, AccountCreateDto accountCreateDto) {
        Account account = accountRepository.getAccountByLogin(login);
        account.setEmail(accountCreateDto.email());
        account.setFirstName(accountCreateDto.firstName());
        account.setLastName(accountCreateDto.lastName());
        accountRepository.saveAndFlush(account);
    }

    public void requestPasswordChange(String email) {
        Optional<Account> account = accountRepository.getAccountByEmail(email);
        if(account.isPresent() && !account.get().isArchived()) {
            passwordResetTokenRepository.saveAndFlush(new PasswordResetToken(account.get()));
            emailService.sendPasswordResetEmail(email);
        }
    }

    public void archiveAccount(UUID id) throws NotFoundException {
        Account account = accountRepository.getAccountById(id).orElseThrow(() -> new NotFoundException(i18n.ACCOUNT_NOT_FOUND));;
        account.setArchived(true);
        accountRepository.saveAndFlush(account);
    }

    public void changePassword(UUID code, String newPassword) throws BadRequestException {
        PasswordResetToken token = passwordResetTokenRepository.getByIdAndExpiresAtAfter(code, LocalDateTime.now()).orElseThrow(() -> new BadRequestException(i18n.INVALID_RESET_CODE));
        Account account = token.getAccount();
        account.setPasswordHash(newPassword); //todo hash
        accountRepository.saveAndFlush(account);
        passwordResetTokenRepository.deletePasswordResetTokenById(code);
    }

    public InviteCode generateInviteCode() {
        InviteCode inviteCode = new InviteCode();
        inviteCodeRepository.saveAndFlush(inviteCode);
        return inviteCode;
    }

    public void createAccount(AccountCreateDto accountCreateDto, UUID inviteCode) throws BadRequestException {
        inviteCodeRepository.existsByIdAndExpiresAtAfter(inviteCode, LocalDateTime.now()).orElseThrow(() -> new BadRequestException(i18n.INVALID_INVITE_CODE));

        if(accountRepository.existsAccountByEmail(accountCreateDto.email())){
            throw new BadRequestException(i18n.EMAIL_IN_USE);
        }
        if(accountRepository.existsAccountByLogin(accountCreateDto.login())){
            throw new BadRequestException(i18n.LOGIN_IN_USE);
        }

        Account account = new Account();
        account.setLogin(accountCreateDto.login());
        account.setEmail(accountCreateDto.email());
        account.setFirstName(accountCreateDto.firstName());
        account.setLastName(accountCreateDto.lastName());
        account.setPasswordHash(accountCreateDto.password()); //todo hash

        Group group = new Group(account.getLogin());
        account.getGroups().add(group);
        group.getAccounts().add(account);

        accountRepository.saveAndFlush(account);
        groupRepository.saveAndFlush(group);
    }
}
