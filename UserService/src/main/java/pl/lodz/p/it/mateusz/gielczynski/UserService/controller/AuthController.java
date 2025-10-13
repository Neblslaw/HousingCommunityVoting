package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.UserService.Exceptions.BadRequestException;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.service.AccountService;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class AuthController {

    private final AccountService accountService;

    @PostMapping("/login")
    public void login() {

    }

    @PostMapping("/register/{inviteCode}")
    public void Register(AccountCreateDto accountCreateDto, @PathVariable UUID inviteCode) throws BadRequestException {
        accountService.createAccount(accountCreateDto, inviteCode);
    }

    @GetMapping("/inviteCode")
    public UUID generateInviteCode() {
        return accountService.generateInviteCode().getId();
    }



}
