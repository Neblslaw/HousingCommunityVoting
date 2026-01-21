package pl.lodz.p.it.mateusz.gielczynski.EstateService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.BuildingMapper;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.dto.BuildingDto;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.service.BuildingService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.ConflictException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.PreconditionFailedException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/estate")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class BuildingController {

    private final BuildingService buildingService;
    private final BuildingMapper buildingMapper;

    @PutMapping
    @PreAuthorize("hasRole('estate:manage')")
    public BuildingDto setBuilding(@RequestBody @Valid BuildingDto buildingDto) throws ConflictException, PreconditionFailedException {
        return buildingMapper.toBuildingDto(buildingService.setBuilding(buildingMapper.toBuilding(buildingDto), buildingDto.id()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('estate:manage')")
    public void deleteBuilding(@PathVariable UUID id) {
        buildingService.deleteBuilding(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('estate:read')")
    public List<BuildingDto> getBuildings() {
        return buildingMapper.toBuildingDto(buildingService.getBuildings());
    }
}
