package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ArchiveAccountIntegrationTest extends IntegrationTestBase {

    private static final UUID VOTER5_ID = UUID.fromString("00000000-0000-0000-0000-000000000005");
    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Test
    @WithMockUser(authorities = "ROLE_accounts:archive")
    void shouldArchiveAccount() throws Exception {
        mockMvc.perform(post("/accounts/{id}/archive", VOTER5_ID))
                .andExpect(status().isOk());

        Account account = testDbHelper.findAccount(VOTER5_ID).orElseThrow();
        assertThat(account.isArchived()).isTrue();
    }

    @Test
    @WithMockUser(authorities = "ROLE_accounts:archive")
    void shouldReturnNotFoundWhenAccountDoesNotExist() throws Exception {
        mockMvc.perform(post("/accounts/{id}/archive", NON_EXISTING_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(post("/accounts/{id}/archive", VOTER5_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/accounts/{id}/archive", VOTER5_ID))
                .andExpect(status().isUnauthorized());
    }
}
