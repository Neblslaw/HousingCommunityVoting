package pl.lodz.p.it.mateusz.gielczynski.UserService.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.postgresql.PostgreSQLContainer;
import pl.lodz.p.it.mateusz.gielczynski.UserService.integration.helper.TestDbHelper;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegrationTestBase {

    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18-alpine").withReuse(true);

    static {
        postgres.start();
    }

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TestDbHelper testDbHelper;
    ObjectMapper objectMapper = new ObjectMapper();
}
