package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.AccountPasswordResetRequestDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class RequestPasswordResetIntegrationTest extends IntegrationTestBase {

    @Test
    @WithMockUser
    void shouldAcceptPasswordResetRequest() throws Exception {
        mockMvc.perform(post("/accounts/password/reset/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AccountPasswordResetRequestDto("admin@cos.pl"))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldAcceptPasswordResetRequestForUnknownEmail() throws Exception {
        mockMvc.perform(post("/accounts/password/reset/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AccountPasswordResetRequestDto("unknown@cos.pl"))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenEmailIsTooShort() throws Exception {
        mockMvc.perform(post("/accounts/password/reset/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AccountPasswordResetRequestDto("a@b"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenEmailIsBlank() throws Exception {
        mockMvc.perform(post("/accounts/password/reset/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AccountPasswordResetRequestDto(""))))
                .andExpect(status().isBadRequest());
    }
}
