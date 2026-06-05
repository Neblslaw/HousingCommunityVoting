package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Role;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class DeleteRoleIntegrationTest extends IntegrationTestBase {

    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Test
    @WithMockUser(authorities = "ROLE_role:delete")
    void shouldDeleteRole() throws Exception {
        Role role = testDbHelper.saveRole(new Role("deletableRole_" + UUID.randomUUID().toString().substring(0, 8)));
        UUID roleId = role.getId();

        mockMvc.perform(delete("/roles/{id}", roleId))
                .andExpect(status().isOk());

        assertThat(testDbHelper.findRole(roleId)).isEmpty();
    }

    @Test
    @WithMockUser(authorities = "ROLE_role:delete")
    void shouldReturnOkWhenRoleDoesNotExist() throws Exception {
        mockMvc.perform(delete("/roles/{id}", NON_EXISTING_ID))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(delete("/roles/{id}", NON_EXISTING_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(delete("/roles/{id}", NON_EXISTING_ID))
                .andExpect(status().isUnauthorized());
    }
}
