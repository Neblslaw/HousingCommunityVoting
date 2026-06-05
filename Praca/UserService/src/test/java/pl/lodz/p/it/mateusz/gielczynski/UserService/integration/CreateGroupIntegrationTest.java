package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.GroupCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CreateGroupIntegrationTest extends IntegrationTestBase {

    @Test
    @WithMockUser(authorities = "ROLE_groups:create")
    void shouldCreateGroup() throws Exception {
        String groupName = "Group_" + UUID.randomUUID().getLeastSignificantBits();

        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new GroupCreateDto(groupName))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.name").value(groupName));

        Group group = testDbHelper.findGroupByName(groupName).orElseThrow();
        assertThat(group.getName()).isEqualTo(groupName);
    }

    @Test
    @WithMockUser(authorities = "ROLE_groups:create")
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new GroupCreateDto(""))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "ROLE_groups:create")
    void shouldReturnBadRequestWhenNameIsTooShort() throws Exception {
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new GroupCreateDto("ab"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "ROLE_groups:create")
    void shouldReturnBadRequestWhenGroupNameAlreadyExists() throws Exception {
        String groupName = "DupGroup_" + UUID.randomUUID().getLeastSignificantBits();
        testDbHelper.saveGroup(new Group(groupName));

        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new GroupCreateDto(groupName))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "ROLE_other:role")
    void shouldReturnUnauthorizedWhenAuthenticatedButLacksRequiredRole() throws Exception {
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new GroupCreateDto("AnyGroup"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new GroupCreateDto("AnyGroup"))))
                .andExpect(status().isUnauthorized());
    }
}
