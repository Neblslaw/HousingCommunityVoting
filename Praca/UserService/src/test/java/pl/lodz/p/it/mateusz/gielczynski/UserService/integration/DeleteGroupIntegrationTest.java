package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class DeleteGroupIntegrationTest extends IntegrationTestBase {

    @Test
    @WithMockUser(authorities = "ROLE_groups:delete")
    void shouldDeleteGroup() throws Exception {
        Group group = testDbHelper.saveGroup(new Group("DeletableGroup_" + UUID.randomUUID()));
        UUID groupId = group.getId();

        mockMvc.perform(delete("/groups/{id}", groupId))
                .andExpect(status().isOk());

        assertThat(testDbHelper.findGroup(groupId)).isEmpty();
    }
}
