package pl.lodz.p.it.mateusz.gielczynski.UserService.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.Date;
import java.util.List;

//todo move to UserService
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final PrivateKey privateKey;
    @Value("${jwt.expiration:360000}")
    private long jwtExpirationInSeconds;
    @Value("${jwt.issuer:http://localhost:8080}")
    private String issuer;

    public String generateToken(String username, List<String> userRoles) {//todo add email
        return Jwts.builder()
                .subject(username)
                .claim("roles", userRoles)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInSeconds))
                .signWith(privateKey)
                .compact();
    }

}

