package pl.lodz.p.it.mateusz.gielczynski.VoteService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class GetVotingIntegrationTest extends IntegrationTestBase {

    private static final UUID BUDGET_VOTING_ID = UUID.fromString("faf0c727-9e65-4776-b841-e28cc93bac8a");
    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Test
    @WithMockUser(authorities = "ROLE_votings:read")
    void shouldGetVoting() throws Exception {
        mockMvc.perform(get("/voting/{id}", BUDGET_VOTING_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BUDGET_VOTING_ID.toString()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_votings:read")
    void shouldReturnNotFoundWhenVotingDoesNotExist() throws Exception {
        mockMvc.perform(get("/voting/{id}", NON_EXISTING_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(get("/voting/{id}", BUDGET_VOTING_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/voting/{id}", BUDGET_VOTING_ID))
                .andExpect(status().isUnauthorized());
    }
}
