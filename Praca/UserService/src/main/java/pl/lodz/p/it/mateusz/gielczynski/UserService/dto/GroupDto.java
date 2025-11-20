package pl.lodz.p.it.mateusz.gielczynski.UserService.dto;

import java.util.UUID;

public record GroupDto(
        UUID id,
        String name,
        int userCount
) {
}
