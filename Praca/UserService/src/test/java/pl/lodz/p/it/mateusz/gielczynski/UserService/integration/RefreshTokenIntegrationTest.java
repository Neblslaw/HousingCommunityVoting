package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.it.mateusz.gielczynski.UserService.jwt.Token;
import pl.lodz.p.it.mateusz.gielczynski.UserService.jwt.TokenProvider;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class RefreshTokenIntegrationTest extends IntegrationTestBase {

    private static final UUID ADMIN_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Autowired
    TokenProvider tokenProvider;

    @Test
    void shouldRefreshToken() throws Exception {
        Token token = tokenProvider.generateToken(ADMIN_ID, "admin", List.of("accounts:read"));

        mockMvc.perform(post("/myauth/refresh")
                        .cookie(new Cookie("voting-refresh", token.refresh())))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()))
                .andExpect(cookie().exists("voting-refresh"));
    }
}
