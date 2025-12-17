package pl.lodz.p.it.mateusz.gielczynski.EstateService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.dto.BuildingDto;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.service.BuildingService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.ConflictException;

import java.util.List;
import java.util.UUID;

import static pl.lodz.p.it.mateusz.gielczynski.EstateService.BuildingMapper.toBuilding;
import static pl.lodz.p.it.mateusz.gielczynski.EstateService.BuildingMapper.toBuildingDto;

@RestController
@RequestMapping("/estate")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class BuildingController {

    private final BuildingService buildingService;

    @PutMapping
    public BuildingDto setBuilding(@RequestBody @Valid BuildingDto buildingDto) throws ConflictException {
        return toBuildingDto(buildingService.setBuilding(toBuilding(buildingDto), buildingDto.id()));
    }

    @DeleteMapping("/{id}")
    public void deleteBuilding(@PathVariable UUID id) {
        buildingService.deleteBuilding(id);
    }

    @GetMapping
    public List<BuildingDto> getBuildings() {
        return toBuildingDto(buildingService.getBuildings());
    }
}
