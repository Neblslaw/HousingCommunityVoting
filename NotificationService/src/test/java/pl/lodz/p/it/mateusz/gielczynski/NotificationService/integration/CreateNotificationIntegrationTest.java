package pl.lodz.p.it.mateusz.gielczynski.NotificationService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.dto.CreateNotificationDTO;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.model.Notification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CreateNotificationIntegrationTest extends IntegrationTestBase {

    @Test
    @WithMockUser(authorities = "ROLE_notifications:create")
    void shouldCreateNotification() throws Exception {
        String text = "Hello_" + java.util.UUID.randomUUID();

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateNotificationDTO(text))))
                .andExpect(status().isOk());

        List<Notification> active = testDbHelper.findActiveNotifications();
        assertThat(active).extracting(Notification::getText).contains(text);
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateNotificationDTO("AnyText"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateNotificationDTO("AnyText"))))
                .andExpect(status().isUnauthorized());
    }
}
