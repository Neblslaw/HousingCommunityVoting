package pl.lodz.p.it.mateusz.gielczynski.VoteService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class GetResultsIntegrationTest extends IntegrationTestBase {

    private static final UUID GARDENING_VOTING_ID = UUID.fromString("41dd2f5f-356e-4578-aa20-6626f074a486");

    @Test
    @WithMockUser(authorities = "ROLE_votings:read")
    void shouldGetResults() throws Exception {
        mockMvc.perform(get("/voting/{id}/results", GARDENING_VOTING_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThan(0)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_votings:read")
    void shouldReturnEmptyListWhenVotingHasNoResults() throws Exception {
        UUID votingWithoutResults = UUID.fromString("faf0c727-9e65-4776-b841-e28cc93bac8a");

        mockMvc.perform(get("/voting/{id}/results", votingWithoutResults))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(get("/voting/{id}/results", GARDENING_VOTING_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/voting/{id}/results", GARDENING_VOTING_ID))
                .andExpect(status().isUnauthorized());
    }
}
