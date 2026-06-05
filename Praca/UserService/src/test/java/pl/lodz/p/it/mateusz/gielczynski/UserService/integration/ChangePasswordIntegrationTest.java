package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountPasswordChangeDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.PasswordResetToken;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ChangePasswordIntegrationTest extends IntegrationTestBase {

    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser
    void shouldChangePassword() throws Exception {
        Account account = createTestAccount("change_pw_");
        PasswordResetToken token = new PasswordResetToken(account);
        testDbHelper.savePasswordResetToken(token);

        String newPassword = "newSecret123!";

        mockMvc.perform(post("/accounts/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AccountPasswordChangeDto(token.getId(), newPassword))))
                .andExpect(status().isOk());

        Account updated = testDbHelper.findAccount(account.getId()).orElseThrow();
        assertThat(passwordEncoder.matches(newPassword, updated.getPasswordHash())).isTrue();
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenResetCodeIsInvalid() throws Exception {
        mockMvc.perform(post("/accounts/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AccountPasswordChangeDto(NON_EXISTING_ID, "newSecret123!"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenNewPasswordIsTooShort() throws Exception {
        Account account = createTestAccount("short_pw_");
        PasswordResetToken token = new PasswordResetToken(account);
        testDbHelper.savePasswordResetToken(token);

        mockMvc.perform(post("/accounts/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AccountPasswordChangeDto(token.getId(), "short"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenNewPasswordIsBlank() throws Exception {
        Account account = createTestAccount("blank_pw_");
        PasswordResetToken token = new PasswordResetToken(account);
        testDbHelper.savePasswordResetToken(token);

        mockMvc.perform(post("/accounts/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AccountPasswordChangeDto(token.getId(), ""))))
                .andExpect(status().isBadRequest());
    }

    private Account createTestAccount(String prefix) {
        String login = prefix + UUID.randomUUID().toString().substring(0, 6);
        return testDbHelper.saveAccount(
                new Account(login, passwordEncoder.encode("oldSecret!"), login + "@cos.pl", "First", "Last", "pl"));
    }
}
