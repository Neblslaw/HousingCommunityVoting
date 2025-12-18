package pl.lodz.p.it.mateusz.gielczynski.UserService.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//todo move to UserService
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final PrivateKey privateKey;
    @Value("${jwt.auth.expiration:360000}")
    private int jwtAuthExpirationInSeconds;
    @Value("${jwt.refresh.expiration:360000000}")
    private int jwtRefreshExpirationInSeconds;
    @Value("${jwt.issuer:http://localhost:8080}")
    private String issuer;


    public Token generateToken(UUID id, String username, List<String> userRoles) {
        return new Token(
                generateToken(id, username, userRoles, jwtAuthExpirationInSeconds),
                generateToken(id, username, userRoles, jwtRefreshExpirationInSeconds)
        );
    }

    private String generateToken(UUID id, String username, List<String> userRoles, long jwtExpirationInSeconds) {//todo add email
        return Jwts.builder()
                .subject(id.toString())
                .claim("name", username)
                .claim("roles", userRoles)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInSeconds))
                .signWith(privateKey)
                .compact();
    }

}

