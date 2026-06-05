package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class LogoutIntegrationTest extends IntegrationTestBase {

    @Test
    void shouldLogoutUser() throws Exception {
        mockMvc.perform(post("/myauth/logout"))
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge("voting-refresh", 0));
    }
}
