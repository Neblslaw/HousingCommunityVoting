package pl.lodz.p.it.mateusz.gielczynski.EstateService.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record BuildingDto(
        @NotEmpty @NotNull List<FloorDto> floors,
        @NotNull String name,
        UUID id,
        Long version
) {
}
