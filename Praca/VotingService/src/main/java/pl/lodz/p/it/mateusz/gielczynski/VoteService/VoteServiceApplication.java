package pl.lodz.p.it.mateusz.gielczynski.VoteService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "pl.lodz.p.it.mateusz.gielczynski")

public class VoteServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(VoteServiceApplication.class, args);
    }

}
