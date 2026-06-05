package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.LoginDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;

import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class LoginIntegrationTest extends IntegrationTestBase {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void shouldLoginUser() throws Exception {
        String password = "Strong!Pass1";
        String login = "loginUser_" + UUID.randomUUID().toString().substring(0, 6);
        Account account = new Account(login, passwordEncoder.encode(password), login + "@cos.pl", "First", "Last", "pl");
        testDbHelper.saveAccount(account);

        mockMvc.perform(post("/myauth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginDto(login, password))))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()))
                .andExpect(cookie().exists("voting-refresh"));
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsWrong() throws Exception {
        String password = "Strong!Pass1";
        String login = "loginUser_" + UUID.randomUUID().toString().substring(0, 6);
        Account account = new Account(login, passwordEncoder.encode(password), login + "@cos.pl", "First", "Last", "pl");
        testDbHelper.saveAccount(account);

        mockMvc.perform(post("/myauth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginDto(login, "wrongPassword"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenLoginIsUnknown() throws Exception {
        mockMvc.perform(post("/myauth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginDto("unknownLogin", "anyPassword"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenLoginIsBlank() throws Exception {
        mockMvc.perform(post("/myauth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginDto("", "Strong!Pass1"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsBlank() throws Exception {
        mockMvc.perform(post("/myauth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginDto("someLogin", ""))))
                .andExpect(status().isBadRequest());
    }
}
