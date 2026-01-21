package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountDetailsDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountPasswordChangeDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountPasswordResetRequestDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.mapper.AccountMapper;
import pl.lodz.p.it.mateusz.gielczynski.UserService.service.AccountService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.BadRequestException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;
import pl.lodz.p.it.mateusz.gielczynski.logger.MethodCallLogged;
import pl.lodz.p.it.mateusz.gielczynski.logger.Sensitive;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
@MethodCallLogged
public class AccountController {

    private final AccountService accountService;


    @GetMapping
    @PreAuthorize("hasRole('accounts:read')")
    public Set<AccountDto> getAccounts() {
        return accountService.getAccounts().stream().map(AccountMapper::toDto).collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('accounts:read')")
    public AccountDetailsDto getAccountById(@PathVariable UUID id) throws NotFoundException {
        return AccountMapper.toDetailedDto(accountService.getAccount(id));
    }

//    @PostMapping()
//    public void updateAccount(AccountCreateDto accountCreateDto, @AuthenticationPrincipal String login) {
//        accountService.updateAccount(login, accountCreateDto);
//    }

    @PostMapping("/password/reset")
    public void changePassword(@RequestBody @Sensitive @Valid AccountPasswordChangeDto accountPasswordChangeDto) throws BadRequestException {
        accountService.changePassword(accountPasswordChangeDto.resetCode(), accountPasswordChangeDto.newPassword());
    }

    @PostMapping("/password/reset/request")
    public void changePassword(@RequestBody @Sensitive @Valid AccountPasswordResetRequestDto accountPasswordResetRequestDto) {
        accountService.requestPasswordChange(accountPasswordResetRequestDto.email());
    }

    @PostMapping("/{id}/archive")
    @PreAuthorize("hasRole('accounts:archive')")
    public void archiveAccount(@PathVariable UUID id) throws NotFoundException {
        accountService.archiveAccount(id);
    }

}
