package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AddUserToGroupIntegrationTest extends IntegrationTestBase {

    private static final UUID VOTER1_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID ESTATE_MANAGER_GROUP_ID = UUID.fromString("32023459-8806-41af-aef0-bd0fb0592b0a");
    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Test
    @WithMockUser(authorities = "ROLE_groups:edit")
    void shouldAddUserToGroup() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/users", ESTATE_MANAGER_GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(VOTER1_ID))))
                .andExpect(status().isOk());

        Account account = testDbHelper.findAccount(VOTER1_ID).orElseThrow();

        assertThat(account.getGroups())
                .extracting(Group::getId)
                .contains(ESTATE_MANAGER_GROUP_ID);
    }

    @Test
    @WithMockUser(authorities = "ROLE_groups:edit")
    void shouldReturnNotFoundWhenGroupDoesNotExist() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/users", NON_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(VOTER1_ID))))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_groups:edit")
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/users", ESTATE_MANAGER_GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(NON_EXISTING_ID))))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/users", ESTATE_MANAGER_GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(VOTER1_ID))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/users", ESTATE_MANAGER_GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(VOTER1_ID))))
                .andExpect(status().isUnauthorized());
    }
}
