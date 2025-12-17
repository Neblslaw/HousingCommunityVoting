package pl.lodz.p.it.mateusz.gielczynski.EstateService.dto;

import java.util.List;

public record FloorDto(
        List<FlatDto> flats,
        int number
) {
}
