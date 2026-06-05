package pl.lodz.p.it.mateusz.gielczynski.EstateService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.model.Building;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class GetBuildingsIntegrationTest extends IntegrationTestBase {

    @Test
    @WithMockUser(authorities = "ROLE_estate:read")
    void shouldGetBuildings() throws Exception {
        String buildingName = "GetTestBuilding_" + UUID.randomUUID();
        testDbHelper.saveBuilding(new Building(buildingName, List.of()));

        mockMvc.perform(get("/estate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThan(0)))
                .andExpect(jsonPath("$[*].name").value(hasItem(buildingName)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(get("/estate"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/estate"))
                .andExpect(status().isUnauthorized());
    }
}
