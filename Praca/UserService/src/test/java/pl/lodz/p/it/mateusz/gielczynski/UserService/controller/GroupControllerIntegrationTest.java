package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.p.it.mateusz.gielczynski.UserService.BaseIntegrationTest;
import pl.lodz.p.it.mateusz.gielczynski.UserService.TestService;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Role;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.RoleRepository;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser
class GroupControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestService testService;

    @Test
    void shouldAddUsersToGroup() throws Exception {
        Group group = new Group("testGroup", Set.of(), 0);

        Account account1 = new Account("user1", "hash1", "user1@test.com", "John", "Doe", "en");
        Account account2 = testService.createAccount(new Account("user2", "hash1", "user2@test.com", "John", "Doe", "en"));

        account1.setGroups(Set.of(group));
        account1 = testService.createAccount(account1);
        group.setUserCount(1);
        group = testService.createGroup(group);


        mockMvc.perform(post("/groups/" + group.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(account1.getId(), account2.getId()))))
                .andExpect(status().isOk());

        assertThat(testService.getGroupById(group.getId()).getUserCount()).isEqualTo(2);
        assertThat(testService.findAllByGroups_Id(group.getId()).containsAll(Set.of(account1, account2))).isTrue();
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
        Group group = new Group("testGroup", Set.of(), 0);

        Role role1 = new Role("boss", 1);
        Role role2 = new Role("worker", 0);

        group.setRoles(Set.of(role1));
        groupRepository.saveAndFlush(group);
        roleRepository.saveAndFlush(role2);

        // When & Then
        mockMvc.perform(post("/groups/" + group.getId() + "/roles/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(role2.getId()))))
                .andExpect(status().isOk());

        assertThat(groupRepository.getGroupById(group.getId()).get().getRoles().containsAll(Set.of(role1, role2))).isTrue();
        assertThat(roleRepository.findRoleById(role1.getId()).get().getGroupsCount()).isEqualTo(1);
        assertThat(roleRepository.findRoleById(role2.getId()).get().getGroupsCount()).isEqualTo(1);
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

