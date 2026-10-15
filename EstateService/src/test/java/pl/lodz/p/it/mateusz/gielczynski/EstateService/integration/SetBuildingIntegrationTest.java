package pl.lodz.p.it.mateusz.gielczynski.EstateService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.JWSHelper;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.dto.BuildingDto;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.dto.FloorDto;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.model.Building;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class SetBuildingIntegrationTest extends IntegrationTestBase {

    @Autowired
    JWSHelper jwsHelper;

    @Test
    @WithMockUser(authorities = "ROLE_estate:manage")
    void shouldCreateBuilding() throws Exception {
        String buildingName = "Building_" + UUID.randomUUID();
        BuildingDto dto = new BuildingDto(
                List.of(new FloorDto(List.of(), 0)),
                buildingName,
                null,
                null,
                null
        );

        mockMvc.perform(put("/estate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        Building building = testDbHelper.findBuildingByName(buildingName).orElseThrow();
        assertThat(building.getName()).isEqualTo(buildingName);
    }

    @Test
    @WithMockUser(authorities = "ROLE_estate:manage")
    void shouldReturnConflictWhenUpdatingWithStaleVersion() throws Exception {
        Building existing = new Building("ConflictBuilding_" + UUID.randomUUID(), List.of());
        existing = testDbHelper.saveBuilding(existing);
        UUID id = existing.getId();
        Long staleVersion = existing.getVersion() + 99L;
        String signature = jwsHelper.generateSignature(id.toString() + staleVersion);
        BuildingDto dto = new BuildingDto(
                List.of(new FloorDto(List.of(), 0)),
                existing.getName() + "_renamed",
                id,
                staleVersion,
                signature
        );

        mockMvc.perform(put("/estate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        BuildingDto dto = new BuildingDto(
                List.of(new FloorDto(List.of(), 0)),
                "SomeBuilding_" + UUID.randomUUID(),
                null,
                null,
                null
        );

        mockMvc.perform(put("/estate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        BuildingDto dto = new BuildingDto(
                List.of(new FloorDto(List.of(), 0)),
                "SomeBuilding_" + UUID.randomUUID(),
                null,
                null,
                null
        );

        mockMvc.perform(put("/estate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }
}
