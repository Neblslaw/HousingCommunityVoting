package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class GetGroupByIdIntegrationTest extends IntegrationTestBase {

    private static final UUID ACCOUNTS_MANAGER_GROUP_ID = UUID.fromString("08cb9e9b-6c5a-44f5-b40d-fd9acbef7b14");
    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Test
    @WithMockUser(authorities = "ROLE_groups:read")
    void shouldGetGroupById() throws Exception {
        mockMvc.perform(get("/groups/{id}", ACCOUNTS_MANAGER_GROUP_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ACCOUNTS_MANAGER_GROUP_ID.toString()))
                .andExpect(jsonPath("$.name").value("Accounts Manager"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_groups:read")
    void shouldReturnNotFoundWhenGroupDoesNotExist() throws Exception {
        mockMvc.perform(get("/groups/{id}", NON_EXISTING_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(get("/groups/{id}", ACCOUNTS_MANAGER_GROUP_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/groups/{id}", ACCOUNTS_MANAGER_GROUP_ID))
                .andExpect(status().isUnauthorized());
    }
}
