package pl.lodz.p.it.mateusz.gielczynski.UserService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

//todo remove
@Configuration
@RequiredArgsConstructor
public class TempAccountGenerator {

    private final StartupService startupService;

    @PostConstruct
    public void init() {
//        startupService.generateAccounts();
    }

}
