package pl.lodz.p.it.mateusz.gielczynski.EstateService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.model.Building;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class DeleteBuildingIntegrationTest extends IntegrationTestBase {

    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Test
    @WithMockUser(authorities = "ROLE_estate:manage")
    void shouldDeleteBuilding() throws Exception {
        Building building = testDbHelper.saveBuilding(new Building("DeletableBuilding_" + UUID.randomUUID(), List.of()));
        UUID buildingId = building.getId();

        mockMvc.perform(delete("/estate/{id}", buildingId))
                .andExpect(status().isOk());

        assertThat(testDbHelper.findBuilding(buildingId)).isEmpty();
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(delete("/estate/{id}", NON_EXISTING_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(delete("/estate/{id}", NON_EXISTING_ID))
                .andExpect(status().isUnauthorized());
    }
}
