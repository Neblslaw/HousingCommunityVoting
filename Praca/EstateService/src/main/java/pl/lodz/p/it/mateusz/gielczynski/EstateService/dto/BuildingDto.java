package pl.lodz.p.it.mateusz.gielczynski.EstateService.dto;

import java.util.List;
import java.util.UUID;

public record BuildingDto(
        List<FloorDto> floors,
        String name,
        UUID id,
        Long version
) {
}
