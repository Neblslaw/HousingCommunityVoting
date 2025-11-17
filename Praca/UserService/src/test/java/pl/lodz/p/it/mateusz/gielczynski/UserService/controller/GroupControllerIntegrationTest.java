package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.p.it.mateusz.gielczynski.UserService.BaseIntegrationTest;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.GroupCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class GroupControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        groupRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void shouldGetAllGroups() throws Exception {
        // When & Then
        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void shouldGetGroupById() throws Exception {
        // Given
        String groupId = "test-group-id";

        // When & Then
        mockMvc.perform(get("/groups/" + groupId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void shouldCreateGroup() throws Exception {
        // Given
        GroupCreateDto groupDto = new GroupCreateDto("Test Group", Set.of("ADMIN", "USER"));

        // When & Then
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void shouldDeleteGroup() throws Exception {
        // Given
        String groupId = "test-group-id";

        // When & Then
        mockMvc.perform(delete("/groups/" + groupId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void shouldAddUserToGroup() throws Exception {
        // Given
        String groupId = "test-group-id";
        String userId = "test-user-id";

        // When & Then
        mockMvc.perform(post("/groups/" + groupId + "/users/" + userId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void shouldRemoveUserFromGroup() throws Exception {
        // Given
        String groupId = "test-group-id";
        String userId = "test-user-id";

        // When & Then
        mockMvc.perform(delete("/groups/" + groupId + "/users/" + userId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void shouldAddRoleToGroup() throws Exception {
        // Given
        String groupId = "test-group-id";
        String roleName = "ADMIN";

        // When & Then
        mockMvc.perform(post("/groups/" + groupId + "/roles/" + roleName))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void shouldRemoveRoleFromGroup() throws Exception {
        // Given
        String groupId = "test-group-id";
        String roleName = "ADMIN";

        // When & Then
        mockMvc.perform(delete("/groups/" + groupId + "/roles/" + roleName))
                .andExpect(status().isOk());
    }
}

