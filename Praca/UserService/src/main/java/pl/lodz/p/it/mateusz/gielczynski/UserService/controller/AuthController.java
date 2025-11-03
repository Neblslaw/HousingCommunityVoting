package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.LoginDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.service.AccountService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.BadRequestException;

import java.util.UUID;

@RestController
@RequestMapping("/myauth")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class AuthController {

    private final AccountService accountService;

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) throws BadRequestException {
        System.out.println("Login");
        return accountService.login(loginDto.login(), loginDto.password());
    }

    //todo remove
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('Zbychu')")
    public String me(@AuthenticationPrincipal Jwt userPrincipal) {
        System.out.println(userPrincipal.getClaims());
        System.out.println(userPrincipal.getSubject());
        return userPrincipal.toString();
    }

    @PostMapping("/register/{inviteCode}")
    public void Register(@RequestBody AccountCreateDto accountCreateDto, @PathVariable UUID inviteCode, @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language) throws BadRequestException {
        accountService.createAccount(accountCreateDto, inviteCode, language);
    }

    @GetMapping("/inviteCode")
    public UUID generateInviteCode() {
        return accountService.generateInviteCode().getId();
    }


}
