package pl.lodz.p.it.mateusz.gielczynski.VoteService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.QuestionDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.VotingDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.VotingGroupDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.mapper.JWSHelper;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Question;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.VotingGroup;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UpdateVotingIntegrationTest extends IntegrationTestBase {

    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Autowired
    JWSHelper jwsHelper;

    private Voting createAndSaveVoting() {
        UUID owner = UUID.randomUUID();
        String name = "UpdateTest_" + UUID.randomUUID();
        Voting voting = new Voting(
                name,
                List.of(new Question("Q1", List.of("a", "b"))),
                owner,
                null,
                null,
                null,
                List.of(new VotingGroup("Group1", List.of(UUID.randomUUID())))
        );
        return testDbHelper.saveVoting(voting);
    }

    @Test
    @WithMockUser(authorities = "ROLE_votings:manage")
    void shouldUpdateVoting() throws Exception {
        Voting voting = createAndSaveVoting();
        UUID id = voting.getId();
        UUID owner = voting.getOwner();
        String signature = jwsHelper.generateSignature(id + "0" + owner);
        String updatedName = "Updated_" + UUID.randomUUID();

        VotingDTO dto = new VotingDTO(
                id,
                0L,
                updatedName,
                List.of(new QuestionDTO(null, "New Q?", List.of("x", "y"))),
                List.of(new VotingGroupDTO("UpdatedGroup", List.of(UUID.randomUUID()))),
                owner,
                null,
                null,
                null,
                signature
        );

        mockMvc.perform(put("/voting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        Voting updated = testDbHelper.findVoting(id).orElseThrow();
        assertThat(updated.getName()).isEqualTo(updatedName);
    }

    @Test
    @WithMockUser(authorities = "ROLE_votings:manage")
    void shouldReturnPreconditionFailedWhenSignatureIsInvalid() throws Exception {
        Voting voting = createAndSaveVoting();
        UUID id = voting.getId();
        UUID owner = voting.getOwner();

        VotingDTO dto = new VotingDTO(
                id,
                0L,
                "Updated_" + UUID.randomUUID(),
                List.of(new QuestionDTO(null, "Q?", List.of("a", "b"))),
                List.of(new VotingGroupDTO("g", List.of(UUID.randomUUID()))),
                owner,
                null,
                null,
                null,
                "invalid-signature"
        );

        mockMvc.perform(put("/voting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    @WithMockUser(authorities = "ROLE_votings:manage")
    void shouldReturnNotFoundWhenVotingDoesNotExist() throws Exception {
        UUID owner = UUID.randomUUID();
        String signature = jwsHelper.generateSignature(NON_EXISTING_ID + "0" + owner);

        VotingDTO dto = new VotingDTO(
                NON_EXISTING_ID,
                0L,
                "NonExisting",
                List.of(new QuestionDTO(null, "Q?", List.of("a", "b"))),
                List.of(new VotingGroupDTO("g", List.of(UUID.randomUUID()))),
                owner,
                null,
                null,
                null,
                signature
        );

        mockMvc.perform(put("/voting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        Voting voting = createAndSaveVoting();
        String signature = jwsHelper.generateSignature(voting.getId() + "0" + voting.getOwner());

        VotingDTO dto = new VotingDTO(
                voting.getId(),
                0L,
                "x",
                List.of(new QuestionDTO(null, "Q?", List.of("a", "b"))),
                List.of(new VotingGroupDTO("g", List.of(UUID.randomUUID()))),
                voting.getOwner(),
                null,
                null,
                null,
                signature
        );

        mockMvc.perform(put("/voting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        Voting voting = createAndSaveVoting();
        String signature = jwsHelper.generateSignature(voting.getId() + "0" + voting.getOwner());

        VotingDTO dto = new VotingDTO(
                voting.getId(),
                0L,
                "x",
                List.of(new QuestionDTO(null, "Q?", List.of("a", "b"))),
                List.of(new VotingGroupDTO("g", List.of(UUID.randomUUID()))),
                voting.getOwner(),
                null,
                null,
                null,
                signature
        );

        mockMvc.perform(put("/voting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }
}
