package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class GenerateInviteCodeIntegrationTest extends IntegrationTestBase {

    @Test
    @WithMockUser(authorities = "ROLE_accounts:invite")
    void shouldGenerateInviteCode() throws Exception {
        mockMvc.perform(get("/myauth/inviteCode"))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(get("/myauth/inviteCode"))
                .andExpect(status().isUnauthorized());
    }
}
