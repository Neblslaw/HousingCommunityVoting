package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.RoleCreateDto;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CreateRoleIntegrationTest extends IntegrationTestBase {

    @Test
    @WithMockUser(authorities = "ROLE_role:create")
    void shouldCreateRole() throws Exception {
        String roleName = "testRole_" + UUID.randomUUID().toString().substring(0, 8);

        mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RoleCreateDto(roleName))))
                .andExpect(status().isOk());

        assertThat(testDbHelper.findRoleByName(roleName)).isPresent();
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RoleCreateDto("someRole"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RoleCreateDto("someRole"))))
                .andExpect(status().isUnauthorized());
    }
}
