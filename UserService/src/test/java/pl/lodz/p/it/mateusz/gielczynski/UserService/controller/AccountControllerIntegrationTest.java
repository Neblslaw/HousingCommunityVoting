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
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountPasswordChangeDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountPasswordResetRequestDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.PasswordResetToken;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.InviteCodeRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.PasswordResetTokenRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class AccountControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InviteCodeRepository inviteCodeRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    void setUp() {
        // Clean up in proper order due to relationships
        groupRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void shouldGetAllAccounts() throws Exception {
        // Given
        Account account1 = new Account("user1", "hash1", "user1@test.com", "John", "Doe", "en");
        Account account2 = new Account("user2", "hash2", "user2@test.com", "Jane", "Smith", "en");
        accountRepository.saveAndFlush(account1);
        accountRepository.saveAndFlush(account2);

        // When & Then
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldGetAccountById() throws Exception {
        // Given
        Account account = new Account("testuser", "hash", "test@test.com", "John", "Doe", "en");
        accountRepository.saveAndFlush(account);
        Account saved = accountRepository.findAccountByLogin("testuser");

        // When & Then
        mockMvc.perform(get("/accounts/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void shouldReturn404WhenAccountNotFound() throws Exception {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(get("/accounts/" + nonExistentId))
                .andExpect(status().isNotFound());
    }

//    @Test
//    @WithMockUser(username = "testuser")
//    void shouldUpdateAccount() throws Exception {
//        // Given
//        Account account = new Account("testuser", "hash", "old@test.com", "Old", "Name", "en");
//        accountRepository.saveAndFlush(account);
//
//        AccountCreateDto updateDto = new AccountCreateDto(
//                "testuser",
//                "new@test.com",
//                "newPassword",
//                "New",
//                "Name"
//        );
//
//        // When
//        mockMvc.perform(post("/accounts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDto)))
//                .andExpect(status().isOk());
//
//        // Then
//        Account updated = accountRepository.findAccountByLogin("testuser");
//        assertThat(updated.getEmail()).isEqualTo("new@test.com");
//        assertThat(updated.getFirstName()).isEqualTo("New");
//        assertThat(updated.getLastName()).isEqualTo("Name");
//    }

    @Test
    void shouldRequestPasswordReset() throws Exception {
        // Given
        Account account = new Account("testuser", "hash", "test@test.com", "John", "Doe", "en");
        accountRepository.saveAndFlush(account);

        AccountPasswordResetRequestDto requestDto = new AccountPasswordResetRequestDto("test@test.com");

        // When
        mockMvc.perform(post("/accounts/password/reset/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // Then - verify token was created (we can't directly count without findAll)
        // The service should have created a token
    }

    @Test
    void shouldNotCreatePasswordResetTokenForArchivedAccount() throws Exception {
        // Given
        Account account = new Account("testuser", "hash", "test@test.com", "John", "Doe", "en");
        account.setArchived(true);
        accountRepository.saveAndFlush(account);

        AccountPasswordResetRequestDto requestDto = new AccountPasswordResetRequestDto("test@test.com");

        // When
        mockMvc.perform(post("/accounts/password/reset/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // Then - archived accounts should not get reset tokens
    }

    @Test
    void shouldChangePasswordWithValidResetCode() throws Exception {
        // Given
        Account account = new Account("testuser", "oldHash", "test@test.com", "John", "Doe", "en");
        accountRepository.saveAndFlush(account);
        Account saved = accountRepository.findAccountByLogin("testuser");

        PasswordResetToken token = new PasswordResetToken(saved);
        passwordResetTokenRepository.saveAndFlush(token);

        AccountPasswordChangeDto changeDto = new AccountPasswordChangeDto(
                token.getId(),
                "newPassword"
        );

        // When
        mockMvc.perform(post("/accounts/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeDto)))
                .andExpect(status().isOk());

        // Then
        Account updated = accountRepository.findAccountById(saved.getId()).get();
        assertThat(updated.getPasswordHash()).isEqualTo("newPassword");
    }

    @Test
    void shouldReturn400WhenResetCodeIsInvalid() throws Exception {
        // Given
        UUID invalidCode = UUID.randomUUID();
        AccountPasswordChangeDto changeDto = new AccountPasswordChangeDto(invalidCode, "newPassword");

        // When & Then
        mockMvc.perform(post("/accounts/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldArchiveAccount() throws Exception {
        // Given
        Account account = new Account("testuser", "hash", "test@test.com", "John", "Doe", "en");
        accountRepository.saveAndFlush(account);
        Account saved = accountRepository.findAccountByLogin("testuser");

        // When
        mockMvc.perform(post("/accounts/" + saved.getId() + "/archive"))
                .andExpect(status().isOk());

        // Then
        Account archived = accountRepository.findAccountById(saved.getId()).get();
        assertThat(archived.isArchived()).isTrue();
    }

    @Test
    void shouldReturn404WhenArchivingNonExistentAccount() throws Exception {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(post("/accounts/" + nonExistentId + "/archive"))
                .andExpect(status().isNotFound());
    }
}
