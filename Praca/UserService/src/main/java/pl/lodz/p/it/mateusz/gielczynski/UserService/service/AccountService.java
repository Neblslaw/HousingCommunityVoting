package pl.lodz.p.it.mateusz.gielczynski.UserService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.UserService.consts.i18n;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.email.EmailService;
import pl.lodz.p.it.mateusz.gielczynski.UserService.jwt.Token;
import pl.lodz.p.it.mateusz.gielczynski.UserService.jwt.TokenProvider;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.InviteCode;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.PasswordResetToken;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.InviteCodeRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.PasswordResetTokenRepository;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.BadRequestException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.InternalServerErrorException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;
import pl.lodz.p.it.mateusz.gielczynski.logger.MethodCallLogged;
import pl.lodz.p.it.mateusz.gielczynski.logger.Sensitive;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@MethodCallLogged
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final InviteCodeRepository inviteCodeRepository;
    private final GroupRepository groupRepository;
    private final TokenProvider tokenProvider;
    private final JwtDecoder jwtDecoder;
    private final PasswordEncoder passwordEncoder;


    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccount(UUID id) throws NotFoundException {
        Account account = accountRepository.findAccountById(id)
                .orElseThrow(() -> new NotFoundException(i18n.ACCOUNT_NOT_FOUND));
        // initialize lazy collections while transaction is open
        account.getGroups().size();
        return account;
    }

    public void updateAccount(String login, AccountCreateDto accountCreateDto) throws InternalServerErrorException {
        Account account = accountRepository.findAccountByLogin(login).orElseThrow(() -> new InternalServerErrorException(""));
        account.setEmail(accountCreateDto.email());
        account.setFirstName(accountCreateDto.firstName());
        account.setLastName(accountCreateDto.lastName());
        accountRepository.saveAndFlush(account);
    }

    public void requestPasswordChange(String email) {
        Optional<Account> account = accountRepository.findAccountByEmail(email);
        if (account.isPresent() && !account.get().isArchived()) {
            passwordResetTokenRepository.saveAndFlush(new PasswordResetToken(account.get()));
            emailService.sendPasswordResetEmail(email);
        }
    }

    public void archiveAccount(UUID id) throws NotFoundException {
        Account account = accountRepository.findAccountById(id).orElseThrow(() -> new NotFoundException(i18n.ACCOUNT_NOT_FOUND));
        account.setArchived(true);
        accountRepository.saveAndFlush(account);
    }

    public void changePassword(UUID code, String newPassword) throws BadRequestException {
        PasswordResetToken token = passwordResetTokenRepository.findByIdAndExpiresAtAfter(code, LocalDateTime.now()).orElseThrow(() -> new BadRequestException(i18n.INVALID_RESET_CODE));
        Account account = token.getAccount();
        account.setPasswordHash(passwordEncoder.encode(newPassword));
        accountRepository.saveAndFlush(account);
        passwordResetTokenRepository.deletePasswordResetTokenById(code);
    }

    public InviteCode generateInviteCode() {
        InviteCode inviteCode = new InviteCode();
        inviteCodeRepository.saveAndFlush(inviteCode);
        return inviteCode;
    }

    public void createAccount(AccountCreateDto accountCreateDto, UUID inviteCode, String language) throws BadRequestException {
        if (!inviteCodeRepository.existsByIdAndExpiresAtAfter(inviteCode, LocalDateTime.now())) {
            throw new BadRequestException(i18n.INVALID_INVITE_CODE);
        }

        if (accountRepository.existsAccountByEmail(accountCreateDto.email())) {
            throw new BadRequestException(i18n.EMAIL_IN_USE);
        }
        if (accountRepository.existsAccountByLogin(accountCreateDto.login())) {
            throw new BadRequestException(i18n.LOGIN_IN_USE);
        }

        Account account = new Account();
        account.setLogin(accountCreateDto.login());
        account.setEmail(accountCreateDto.email());
        account.setFirstName(accountCreateDto.firstName());
        account.setLastName(accountCreateDto.lastName());
        account.setPasswordHash(passwordEncoder.encode(accountCreateDto.password()));
        account.setLanguage(language);

        try {
            accountRepository.saveAndFlush(account);
        } catch (DataIntegrityViolationException e) { //todo check if its only possible violation
            throw new BadRequestException(i18n.LOGIN_IN_USE);
        }
    }

    @Sensitive
    public Token login(String login, @Sensitive String password) throws BadRequestException {
        Account account = accountRepository.findAccountByLogin(login).orElseThrow(() -> new BadRequestException(i18n.CREDENTIALS_DO_NOT_MATCH));
        if (passwordEncoder.matches(password, account.getPasswordHash())) {
            return tokenProvider.generateToken(account.getId(), account.getLogin(), account.getRoles());
        }
        throw new BadRequestException(i18n.CREDENTIALS_DO_NOT_MATCH);
    }


    public Token refreshToken(String refreshToken) throws BadRequestException {
        Jwt jwt = jwtDecoder.decode(refreshToken);
        Account account = accountRepository.findAccountById(UUID.fromString(jwt.getSubject())).orElseThrow(() -> new BadRequestException(i18n.ACCOUNT_NOT_FOUND));
        return tokenProvider.generateToken(account.getId(), account.getLogin(), account.getRoles());
    }
}
