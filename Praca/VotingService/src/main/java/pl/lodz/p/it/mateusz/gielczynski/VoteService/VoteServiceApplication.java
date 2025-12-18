package pl.lodz.p.it.mateusz.gielczynski.VoteService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.RollbackOn;

@SpringBootApplication(scanBasePackages = "pl.lodz.p.it.mateusz.gielczynski")
@EnableJpaAuditing
@EnableTransactionManagement(rollbackOn = RollbackOn.ALL_EXCEPTIONS)
public class VoteServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(VoteServiceApplication.class, args);
    }

}
