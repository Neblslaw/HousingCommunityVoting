package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class GetAccountByIdIntegrationTest extends IntegrationTestBase {

    private static final UUID ADMIN_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Test
    @WithMockUser(authorities = "ROLE_accounts:read")
    void shouldGetAccountById() throws Exception {
        mockMvc.perform(get("/accounts/{id}", ADMIN_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ADMIN_ID.toString()))
                .andExpect(jsonPath("$.login").value("admin"))
                .andExpect(jsonPath("$.email").value("admin@cos.pl"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_accounts:read")
    void shouldReturnNotFoundWhenAccountDoesNotExist() throws Exception {
        mockMvc.perform(get("/accounts/{id}", NON_EXISTING_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(get("/accounts/{id}", ADMIN_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/accounts/{id}", ADMIN_ID))
                .andExpect(status().isUnauthorized());
    }
}
