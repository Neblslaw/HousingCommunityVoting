package pl.lodz.p.it.mateusz.gielczynski.UserService.dto;

import java.util.UUID;

public record AccountPasswordChangeDto (
        UUID resetCode,
        String newPassword
) {
}
