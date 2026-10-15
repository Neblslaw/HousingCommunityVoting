package pl.lodz.p.it.mateusz.gielczynski.VoteService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Question;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.VotingGroup;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class LockVotingIntegrationTest extends IntegrationTestBase {

    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    private Voting createUnlockedVoting() {
        UUID owner = UUID.randomUUID();
        return testDbHelper.saveVoting(new Voting(
                "LockTest_" + UUID.randomUUID(),
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
    void shouldLockVoting() throws Exception {
        Voting voting = createUnlockedVoting();

        mockMvc.perform(post("/voting/{id}/lock", voting.getId()))
                .andExpect(status().isOk());

        Voting locked = testDbHelper.findVoting(voting.getId()).orElseThrow();
        assertThat(locked.getLockTime()).isNotNull();
    }

    @Test
    @WithMockUser(authorities = "ROLE_votings:manage")
    void shouldReturnNotFoundWhenVotingDoesNotExist() throws Exception {
        mockMvc.perform(post("/voting/{id}/lock", NON_EXISTING_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        Voting voting = createUnlockedVoting();

        mockMvc.perform(post("/voting/{id}/lock", voting.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        Voting voting = createUnlockedVoting();

        mockMvc.perform(post("/voting/{id}/lock", voting.getId()))
                .andExpect(status().isUnauthorized());
    }
}
