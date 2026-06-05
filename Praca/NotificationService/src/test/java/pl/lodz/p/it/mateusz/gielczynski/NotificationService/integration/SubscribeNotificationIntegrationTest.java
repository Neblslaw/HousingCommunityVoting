package pl.lodz.p.it.mateusz.gielczynski.NotificationService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class SubscribeNotificationIntegrationTest extends IntegrationTestBase {

    @Test
    @WithMockUser(authorities = "ROLE_notifications:read")
    void shouldReturnInternalServerErrorWhenJwtPrincipalIsNull() throws Exception {
        mockMvc.perform(get("/notifications/subscribe"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(get("/notifications/subscribe"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/notifications/subscribe"))
                .andExpect(status().isUnauthorized());
    }
}
