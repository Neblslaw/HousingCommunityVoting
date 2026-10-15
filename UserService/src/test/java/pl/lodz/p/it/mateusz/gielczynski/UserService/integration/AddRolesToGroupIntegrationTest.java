package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Role;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AddRolesToGroupIntegrationTest extends IntegrationTestBase {

    private static final UUID NOTIFICATIONS_MANAGER_GROUP_ID = UUID.fromString("b902090f-9c37-4610-b56e-e6bfedf3f779");
    private static final UUID ESTATE_READ_ROLE_ID = UUID.fromString("c4e1cb5c-eb3a-4125-bb3a-0b0f6bbaf9dc");
    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Test
    @WithMockUser(authorities = "ROLE_groups:edit")
    void shouldAddRolesToGroup() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/roles", NOTIFICATIONS_MANAGER_GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(ESTATE_READ_ROLE_ID))))
                .andExpect(status().isOk());

        Group group = testDbHelper.findGroup(NOTIFICATIONS_MANAGER_GROUP_ID).orElseThrow();

        assertThat(group.getRoles())
                .extracting(Role::getId)
                .contains(ESTATE_READ_ROLE_ID);
    }

    @Test
    @WithMockUser(authorities = "ROLE_groups:edit")
    void shouldReturnNotFoundWhenGroupDoesNotExist() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/roles", NON_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(ESTATE_READ_ROLE_ID))))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_groups:edit")
    void shouldReturnNotFoundWhenRoleDoesNotExist() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/roles", NOTIFICATIONS_MANAGER_GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(NON_EXISTING_ID))))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/roles", NOTIFICATIONS_MANAGER_GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(ESTATE_READ_ROLE_ID))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/roles", NOTIFICATIONS_MANAGER_GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(ESTATE_READ_ROLE_ID))))
                .andExpect(status().isUnauthorized());
    }
}
