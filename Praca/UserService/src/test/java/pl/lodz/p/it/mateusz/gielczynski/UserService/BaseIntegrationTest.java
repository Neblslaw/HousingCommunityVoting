package pl.lodz.p.it.mateusz.gielczynski.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.InviteCodeRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.PasswordResetTokenRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    static {
        postgres.start();
    }

    @LocalServerPort
    protected int port;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    InviteCodeRepository inviteCodeRepository;

    @BeforeEach
    void setUp() {
        // Clean up in proper order due to relationships
        passwordResetTokenRepository.deleteAll();
        accountRepository.deleteAll();
        groupRepository.deleteAll();
        inviteCodeRepository.deleteAll();

    }
}
