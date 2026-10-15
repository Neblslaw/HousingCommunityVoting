package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Role;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class RemoveRolesFromGroupIntegrationTest extends IntegrationTestBase {

    private static final UUID ESTATE_MANAGER_GROUP_ID = UUID.fromString("32023459-8806-41af-aef0-bd0fb0592b0a");
    private static final UUID ACCOUNTS_READ_ROLE_ID = UUID.fromString("3d67ff94-745b-4cd6-9e37-9d992b98f21c");
    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Test
    @WithMockUser(authorities = "ROLE_groups:edit")
    void shouldRemoveRolesFromGroup() throws Exception {
        mockMvc.perform(delete("/groups/{groupId}/roles", ESTATE_MANAGER_GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(ACCOUNTS_READ_ROLE_ID))))
                .andExpect(status().isOk());

        Group group = testDbHelper.findGroup(ESTATE_MANAGER_GROUP_ID).orElseThrow();

        assertThat(group.getRoles())
                .extracting(Role::getId)
                .doesNotContain(ACCOUNTS_READ_ROLE_ID);
    }

    @Test
    @WithMockUser(authorities = "ROLE_groups:edit")
    void shouldReturnNotFoundWhenGroupDoesNotExist() throws Exception {
        mockMvc.perform(delete("/groups/{groupId}/roles", NON_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(ACCOUNTS_READ_ROLE_ID))))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_groups:edit")
    void shouldReturnNotFoundWhenRoleDoesNotExist() throws Exception {
        mockMvc.perform(delete("/groups/{groupId}/roles", ESTATE_MANAGER_GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(NON_EXISTING_ID))))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(delete("/groups/{groupId}/roles", ESTATE_MANAGER_GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(ACCOUNTS_READ_ROLE_ID))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(delete("/groups/{groupId}/roles", ESTATE_MANAGER_GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(ACCOUNTS_READ_ROLE_ID))))
                .andExpect(status().isUnauthorized());
    }
}
