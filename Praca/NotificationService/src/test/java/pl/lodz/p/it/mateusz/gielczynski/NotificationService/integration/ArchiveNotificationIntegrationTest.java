package pl.lodz.p.it.mateusz.gielczynski.NotificationService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.model.NOTIFICATION_STATE;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.model.Notification;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ArchiveNotificationIntegrationTest extends IntegrationTestBase {

    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Test
    @WithMockUser(authorities = "ROLE_notifications:archive")
    void shouldArchiveNotification() throws Exception {
        Notification notification = new Notification();
        notification.setText("ToArchive_" + UUID.randomUUID());
        testDbHelper.saveNotification(notification);
        UUID id = notification.getId();

        mockMvc.perform(post("/notifications/{id}/archive", id))
                .andExpect(status().isOk());

        Notification archived = testDbHelper.findNotification(id).orElseThrow();
        assertThat(archived.getState()).isEqualTo(NOTIFICATION_STATE.ARCHIVED);
    }

    @Test
    @WithMockUser(authorities = "ROLE_notifications:archive")
    void shouldReturnNotFoundWhenNotificationDoesNotExist() throws Exception {
        mockMvc.perform(post("/notifications/{id}/archive", NON_EXISTING_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(post("/notifications/{id}/archive", NON_EXISTING_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/notifications/{id}/archive", NON_EXISTING_ID))
                .andExpect(status().isUnauthorized());
    }
}
