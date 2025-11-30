package pl.lodz.p.it.mateusz.gielczynski.VoteService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "pl.lodz.p.it.mateusz.gielczynski")
@EnableJpaAuditing
public class VoteServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(VoteServiceApplication.class, args);
    }

}
