package pl.lodz.p.it.mateusz.gielczynski.EstateService.dto;

import java.util.List;
import java.util.UUID;

public record FlatDto(
        String number,
        int order,
        List<UUID> inhabitants
) {
}
