package pl.lodz.p.it.mateusz.gielczynski.UserService.dto;

import java.util.UUID;

public record AccountDto(
        UUID id,
        String login,
        String email,
        String firstName,
        String lastName
) {
}
