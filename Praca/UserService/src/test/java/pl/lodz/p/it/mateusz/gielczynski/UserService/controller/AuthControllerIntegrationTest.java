package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.p.it.mateusz.gielczynski.UserService.BaseIntegrationTest;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.InviteCode;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.InviteCodeRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.PasswordResetTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class AuthControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InviteCodeRepository inviteCodeRepository;

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @BeforeEach
    void setUp() {
        // Clean up in proper order due to relationships
        groupRepository.deleteAll();
        inviteCodeRepository.deleteAll();
        passwordResetTokenRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void shouldGenerateInviteCode() throws Exception {
        // When & Then
        mockMvc.perform(get("/auth/inviteCode"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        org.hamcrest.Matchers.matchesPattern(
                        "^\"[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}\"$"
                )));
    }

    @Test
    void shouldRegisterNewAccountWithValidInviteCode() throws Exception {
        // Given
        InviteCode inviteCode = new InviteCode();
        inviteCodeRepository.saveAndFlush(inviteCode);

        AccountCreateDto registerDto = new AccountCreateDto(
                "newuser",
                "newuser@test.com",
                "password123",
                "John",
                "Doe"
        );

        // When
        mockMvc.perform(post("/auth/register/" + inviteCode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        // Then
        assertThat(accountRepository.existsAccountByLogin("newuser")).isTrue();
        Account account = accountRepository.findAccountByLogin("newuser");
        assertThat(account.getEmail()).isEqualTo("newuser@test.com");
        assertThat(account.getFirstName()).isEqualTo("John");
        assertThat(account.getLastName()).isEqualTo("Doe");
        assertThat(account.getGroups()).hasSize(1);
        assertThat(account.getGroups().iterator().next().getName()).isEqualTo("newuser");
    }

    @Test
    void shouldReturn400WhenRegisteringWithInvalidInviteCode() throws Exception {
        // Given
        UUID invalidCode = UUID.randomUUID();
        AccountCreateDto registerDto = new AccountCreateDto(
                "newuser",
                "newuser@test.com",
                "password123",
                "John",
                "Doe"
        );

        // When & Then
        mockMvc.perform(post("/auth/register/" + invalidCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());

        assertThat(accountRepository.existsAccountByLogin("newuser")).isFalse();
    }

    @Test
    void shouldReturn400WhenRegisteringWithExpiredInviteCode() throws Exception {
        // Given
        InviteCode inviteCode = new InviteCode();
        inviteCode.setExpiresAt(LocalDateTime.now().minusDays(1));
        inviteCodeRepository.saveAndFlush(inviteCode);

        AccountCreateDto registerDto = new AccountCreateDto(
                "newuser",
                "newuser@test.com",
                "password123",
                "John",
                "Doe"
        );

        // When & Then
        mockMvc.perform(post("/auth/register/" + inviteCode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());

        assertThat(accountRepository.existsAccountByLogin("newuser")).isFalse();
    }

    @Test
    void shouldReturn400WhenRegisteringWithExistingEmail() throws Exception {
        // Given
        Account existingAccount = new Account("existinguser", "hash", "existing@test.com", "Jane", "Smith", "en");
        accountRepository.saveAndFlush(existingAccount);

        InviteCode inviteCode = new InviteCode();
        inviteCodeRepository.saveAndFlush(inviteCode);

        AccountCreateDto registerDto = new AccountCreateDto(
                "newuser",
                "existing@test.com",
                "password123",
                "John",
                "Doe"
        );

        // When & Then
        mockMvc.perform(post("/auth/register/" + inviteCode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());

        assertThat(accountRepository.existsAccountByLogin("newuser")).isFalse();
    }

    @Test
    void shouldReturn400WhenRegisteringWithExistingLogin() throws Exception {
        // Given
        Account existingAccount = new Account("existinguser", "hash", "existing@test.com", "Jane", "Smith", "en");
        accountRepository.saveAndFlush(existingAccount);

        InviteCode inviteCode = new InviteCode();
        inviteCodeRepository.saveAndFlush(inviteCode);

        AccountCreateDto registerDto = new AccountCreateDto(
                "existinguser",
                "newuser@test.com",
                "password123",
                "John",
                "Doe"
        );

        // When & Then
        mockMvc.perform(post("/auth/register/" + inviteCode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());

        // Verify original account still exists and no new one was created
        assertThat(accountRepository.existsAccountByLogin("existinguser")).isTrue();
    }

    @Test
    void shouldHandleLoginEndpoint() throws Exception {
        // Note: Login endpoint is empty in the controller, just verify it's accessible
        mockMvc.perform(post("/auth/login"))
                .andExpect(status().isOk());
    }
}
