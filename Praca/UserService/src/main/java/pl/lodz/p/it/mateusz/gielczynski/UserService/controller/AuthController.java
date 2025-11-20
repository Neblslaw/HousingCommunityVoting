package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.LoginDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.service.AccountService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.BadRequestException;
import pl.lodz.p.it.mateusz.gielczynski.logger.MethodCallLogged;
import pl.lodz.p.it.mateusz.gielczynski.logger.Sensitive;

import java.util.UUID;

@RestController
@RequestMapping("/myauth")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
@MethodCallLogged
public class AuthController {

    private final AccountService accountService;

    @PostMapping("/login")
    public String login(@RequestBody @Sensitive @Valid LoginDto loginDto) throws BadRequestException {
        System.out.println("Login");
        return accountService.login(loginDto.login(), loginDto.password());
    }

    @PostMapping("/register/{inviteCode}")
    public void Register(@RequestBody @Valid @Sensitive AccountCreateDto accountCreateDto, @PathVariable @Sensitive UUID inviteCode, @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language) throws BadRequestException {
        accountService.createAccount(accountCreateDto, inviteCode, language);
    }

    @GetMapping("/inviteCode")
    public UUID generateInviteCode() {
        return accountService.generateInviteCode().getId();
    }


}
