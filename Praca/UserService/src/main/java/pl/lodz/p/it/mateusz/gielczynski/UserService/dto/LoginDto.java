package pl.lodz.p.it.mateusz.gielczynski.UserService.dto;

import pl.lodz.p.it.mateusz.gielczynski.logger.Sensitive;

public record LoginDto(
        String login,
        @Sensitive String password
) {
}
