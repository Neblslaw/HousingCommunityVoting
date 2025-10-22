package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.p.it.mateusz.gielczynski.UserService.BaseIntegrationTest;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.RoleCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class RoleControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    void setUp() {
        groupRepository.deleteAll();
    }

    @Test
    void shouldGetAllRoles() throws Exception {
        // When & Then
        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldCreateRole() throws Exception {
        // Given
        RoleCreateDto roleDto = new RoleCreateDto("ADMIN");

        // When & Then
        mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteRole() throws Exception {
        // Given
        String roleName = "ADMIN";

        // When & Then
        mockMvc.perform(delete("/roles")
                        .param("roleName", roleName))
                .andExpect(status().isOk());
    }
}
