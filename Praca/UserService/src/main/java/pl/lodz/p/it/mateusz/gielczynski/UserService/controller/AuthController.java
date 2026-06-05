package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.LoginDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.jwt.Token;
import pl.lodz.p.it.mateusz.gielczynski.UserService.service.AccountService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.ApplicationException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.BadRequestException;
import pl.lodz.p.it.mateusz.gielczynski.logger.MethodCallLogged;
import pl.lodz.p.it.mateusz.gielczynski.logger.Sensitive;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/myauth")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
@MethodCallLogged
public class AuthController {

    private static final String REFRESH_COOKIE_NAME = "voting-refresh";
    private final AccountService accountService;
    @Value("${jwt.refresh.expiration:360000000}")
    private int jwtRefreshExpirationInSeconds;

    @PostMapping("/login")
    @Sensitive
    public String login(@RequestBody @Sensitive @Valid LoginDto loginDto, HttpServletResponse response) throws BadRequestException {
        Token token = accountService.login(loginDto.login(), loginDto.password());

        Cookie refreshCookie = new Cookie(REFRESH_COOKIE_NAME, token.refresh());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(jwtRefreshExpirationInSeconds);
        refreshCookie.setAttribute("SameSite", SameSiteCookies.STRICT.toString());
        response.addCookie(refreshCookie);

        return token.auth();
    }

    @PostMapping("/refresh")
    public String refresh(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {

        Cookie refreshCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(REFRESH_COOKIE_NAME))
                .toList().stream().findFirst().orElseThrow(() -> new ApplicationException("no refresh", "unauthorized", HttpStatus.UNAUTHORIZED));

        Token token = accountService.refreshToken(refreshCookie.getValue());


        Cookie newRefreshCookie = new Cookie(REFRESH_COOKIE_NAME, token.refresh());
        newRefreshCookie.setHttpOnly(true);
        newRefreshCookie.setSecure(true);
        newRefreshCookie.setPath("/");
        newRefreshCookie.setMaxAge(jwtRefreshExpirationInSeconds);
        newRefreshCookie.setAttribute("SameSite", SameSiteCookies.STRICT.toString());
        response.addCookie(newRefreshCookie);

        return token.auth();
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie refreshCookie = new Cookie(REFRESH_COOKIE_NAME, "");
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);
    }

    @PostMapping("/register/{inviteCode}")
    public void Register(@RequestBody @Valid @Sensitive AccountCreateDto accountCreateDto, @PathVariable @Sensitive UUID inviteCode, @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language) throws BadRequestException {
        accountService.createAccount(accountCreateDto, inviteCode, language);
    }

    @GetMapping("/inviteCode")
    @PreAuthorize("hasRole('accounts:invite')")
    public UUID generateInviteCode() {
        return accountService.generateInviteCode().getId();
    }


}
