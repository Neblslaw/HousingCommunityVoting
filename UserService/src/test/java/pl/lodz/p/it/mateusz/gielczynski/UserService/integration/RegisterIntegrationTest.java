package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.InviteCode;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class RegisterIntegrationTest extends IntegrationTestBase {

    private static final UUID NON_EXISTING_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Test
    void shouldRegisterUser() throws Exception {
        InviteCode inviteCode = new InviteCode();
        testDbHelper.saveInviteCode(inviteCode);

        String login = "regUser_" + UUID.randomUUID().toString().substring(0, 6);
        AccountCreateDto dto = new AccountCreateDto(login, login + "@cos.pl", "Strong!Pass1", "First", "Last");

        mockMvc.perform(post("/myauth/register/{inviteCode}", inviteCode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "pl")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        Account account = testDbHelper.findAccountByLogin(login).orElseThrow();
        assertThat(account.getEmail()).isEqualTo(login + "@cos.pl");
        assertThat(account.getFirstName()).isEqualTo("First");
        assertThat(account.getLastName()).isEqualTo("Last");
    }

    @Test
    void shouldReturnBadRequestWhenInviteCodeIsInvalid() throws Exception {
        AccountCreateDto dto = new AccountCreateDto("regUser_x", "regUser_x@cos.pl", "Strong!Pass1", "First", "Last");

        mockMvc.perform(post("/myauth/register/{inviteCode}", NON_EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "pl")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsAlreadyInUse() throws Exception {
        InviteCode inviteCode = new InviteCode();
        testDbHelper.saveInviteCode(inviteCode);

        AccountCreateDto dto = new AccountCreateDto("regUser_y", "admin@cos.pl", "Strong!Pass1", "First", "Last");

        mockMvc.perform(post("/myauth/register/{inviteCode}", inviteCode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "pl")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenLoginIsAlreadyInUse() throws Exception {
        InviteCode inviteCode = new InviteCode();
        testDbHelper.saveInviteCode(inviteCode);

        AccountCreateDto dto = new AccountCreateDto("admin", "newEmail@cos.pl", "Strong!Pass1", "First", "Last");

        mockMvc.perform(post("/myauth/register/{inviteCode}", inviteCode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "pl")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsTooShort() throws Exception {
        InviteCode inviteCode = new InviteCode();
        testDbHelper.saveInviteCode(inviteCode);

        AccountCreateDto dto = new AccountCreateDto("regUser_z", "regUser_z@cos.pl", "short", "First", "Last");

        mockMvc.perform(post("/myauth/register/{inviteCode}", inviteCode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "pl")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenLoginIsBlank() throws Exception {
        InviteCode inviteCode = new InviteCode();
        testDbHelper.saveInviteCode(inviteCode);

        AccountCreateDto dto = new AccountCreateDto("", "blanklogin@cos.pl", "Strong!Pass1", "First", "Last");

        mockMvc.perform(post("/myauth/register/{inviteCode}", inviteCode.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "pl")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
