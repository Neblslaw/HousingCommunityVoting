package pl.lodz.p.it.mateusz.gielczynski.VoteService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Question;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.VotingGroup;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class DeleteVotingIntegrationTest extends IntegrationTestBase {

    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    private Voting createVoting() {
        UUID owner = UUID.randomUUID();
        return testDbHelper.saveVoting(new Voting(
                "DeleteTest_" + UUID.randomUUID(),
                List.of(new Question("Q?", List.of("a", "b"))),
                owner,
                null,
                null,
                null,
                List.of(new VotingGroup("g", List.of(UUID.randomUUID())))
        ));
    }

    @Test
    @WithMockUser(authorities = "ROLE_votings:manage")
    void shouldDeleteVoting() throws Exception {
        Voting voting = createVoting();

        mockMvc.perform(delete("/voting/{id}", voting.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_votings:manage")
    void shouldReturnNotFoundWhenVotingDoesNotExist() throws Exception {
        mockMvc.perform(delete("/voting/{id}", NON_EXISTING_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        Voting voting = createVoting();

        mockMvc.perform(delete("/voting/{id}", voting.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        Voting voting = createVoting();

        mockMvc.perform(delete("/voting/{id}", voting.getId()))
                .andExpect(status().isUnauthorized());
    }
}
