package pl.lodz.p.it.mateusz.gielczynski.VoteService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class GetVotingsByOwnerIntegrationTest extends IntegrationTestBase {

    private static final UUID OWNER_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Test
    void shouldGetVotingsByOwner() throws Exception {
        mockMvc.perform(get("/voting/owned")
                        .with(jwt()
                                .jwt(builder -> builder.claim("sub", OWNER_ID.toString()))
                                .authorities(new SimpleGrantedAuthority("ROLE_votings:manage"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThan(0)));
    }

    @Test
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(get("/voting/owned")
                        .with(jwt()
                                .jwt(builder -> builder.claim("sub", OWNER_ID.toString()))
                                .authorities(new SimpleGrantedAuthority("ROLE_other:role"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/voting/owned"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ROLE_votings:manage")
    void shouldReturnInternalServerErrorWhenJwtPrincipalIsMissing() throws Exception {
        mockMvc.perform(get("/voting/owned"))
                .andExpect(status().isInternalServerError());
    }
}
