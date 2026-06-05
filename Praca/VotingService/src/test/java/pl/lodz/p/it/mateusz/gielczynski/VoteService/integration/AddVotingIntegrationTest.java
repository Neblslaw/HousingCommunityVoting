package pl.lodz.p.it.mateusz.gielczynski.VoteService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.QuestionDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.VotingDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.VotingGroupDTO;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AddVotingIntegrationTest extends IntegrationTestBase {

    @Test
    @WithMockUser(authorities = "ROLE_votings:manage")
    void shouldAddVoting() throws Exception {
        String name = "Voting_" + UUID.randomUUID();
        UUID owner = UUID.randomUUID();
        VotingDTO dto = new VotingDTO(
                null,
                null,
                name,
                List.of(new QuestionDTO(null, "Question?", List.of("Yes", "No"))),
                List.of(new VotingGroupDTO("GroupA", List.of(UUID.randomUUID()))),
                owner,
                null,
                null,
                null,
                null
        );

        mockMvc.perform(post("/voting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    @WithMockUser(authorities = "ROLE_votings:manage")
    void shouldReturnBadRequestWhenQuestionsAreEmpty() throws Exception {
        VotingDTO dto = new VotingDTO(
                null,
                null,
                "Voting_" + UUID.randomUUID(),
                List.of(),
                List.of(new VotingGroupDTO("GroupA", List.of(UUID.randomUUID()))),
                UUID.randomUUID(),
                null,
                null,
                null,
                null
        );

        mockMvc.perform(post("/voting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "ROLE_votings:manage")
    void shouldReturnBadRequestWhenVotingGroupsAreEmpty() throws Exception {
        VotingDTO dto = new VotingDTO(
                null,
                null,
                "Voting_" + UUID.randomUUID(),
                List.of(new QuestionDTO(null, "Question?", List.of("Yes", "No"))),
                List.of(),
                UUID.randomUUID(),
                null,
                null,
                null,
                null
        );

        mockMvc.perform(post("/voting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        VotingDTO dto = new VotingDTO(
                null,
                null,
                "Voting_" + UUID.randomUUID(),
                List.of(new QuestionDTO(null, "Question?", List.of("Yes", "No"))),
                List.of(new VotingGroupDTO("GroupA", List.of(UUID.randomUUID()))),
                UUID.randomUUID(),
                null,
                null,
                null,
                null
        );

        mockMvc.perform(post("/voting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        VotingDTO dto = new VotingDTO(
                null,
                null,
                "Voting_" + UUID.randomUUID(),
                List.of(new QuestionDTO(null, "Question?", List.of("Yes", "No"))),
                List.of(new VotingGroupDTO("GroupA", List.of(UUID.randomUUID()))),
                UUID.randomUUID(),
                null,
                null,
                null,
                null
        );

        mockMvc.perform(post("/voting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }
}
