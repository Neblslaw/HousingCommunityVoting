package pl.lodz.p.it.mateusz.gielczynski.VoteService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class GetVotingsByAvailableIntegrationTest extends IntegrationTestBase {

    private static final UUID VOTER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Test
    void shouldGetVotingsByAvailable() throws Exception {
        mockMvc.perform(get("/voting/available")
                        .with(jwt()
                                .jwt(builder -> builder.claim("sub", VOTER_ID.toString()))
                                .authorities(new SimpleGrantedAuthority("ROLE_votings:participate"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(get("/voting/available")
                        .with(jwt()
                                .jwt(builder -> builder.claim("sub", VOTER_ID.toString()))
                                .authorities(new SimpleGrantedAuthority("ROLE_other:role"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/voting/available"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ROLE_votings:participate")
    void shouldReturnInternalServerErrorWhenJwtPrincipalIsMissing() throws Exception {
        mockMvc.perform(get("/voting/available"))
                .andExpect(status().isInternalServerError());
    }
}
