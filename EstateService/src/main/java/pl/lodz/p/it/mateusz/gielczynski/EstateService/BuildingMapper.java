package pl.lodz.p.it.mateusz.gielczynski.EstateService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.dto.BuildingDto;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.dto.FlatDto;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.dto.FloorDto;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.model.Building;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.model.Flat;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.model.Floor;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.PreconditionFailedException;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BuildingMapper {

    private final JWSHelper jwsHelper;

    private static List<FloorDto> toFloorDto(List<Floor> floors) {
        return floors.stream().map(BuildingMapper::toFloorDto).toList();
    }

    private static FloorDto toFloorDto(Floor floor) {
        return new FloorDto(toFlatDto(floor.getFlats()), floor.getNumber());
    }

    private static List<FlatDto> toFlatDto(List<Flat> flats) {
        return flats.stream().map(BuildingMapper::toFlatDto).toList();
    }

    private static FlatDto toFlatDto(Flat flat) {
        return new FlatDto(flat.getNumber(), flat.getOrder(), flat.getInhabitants());
    }

    private static List<Floor> toFloor(List<FloorDto> dtos) {
        return dtos.stream().map(BuildingMapper::toFloor).toList();
    }

    private static Floor toFloor(FloorDto floorDto) {
        return new Floor(toFlat(floorDto.flats()), floorDto.number());
    }

    private static List<Flat> toFlat(List<FlatDto> dtos) {
        return dtos.stream().map(BuildingMapper::toFlat).toList();
    }

    private static Flat toFlat(FlatDto flatDto) {
        return new Flat(flatDto.number(), flatDto.order(), flatDto.inhabitants());
    }

    public BuildingDto toBuildingDto(Building building) {
        return new BuildingDto(toFloorDto(building.getFloors()), building.getName(), building.getId(), building.getVersion(), jwsHelper.generateSignature(building.getId().toString() + building.getVersion()));
    }

    public List<BuildingDto> toBuildingDto(List<Building> buildings) {
        return buildings.stream().map(this::toBuildingDto).toList();
    }

    public Building toBuilding(BuildingDto buildingDto) throws PreconditionFailedException {
        if (buildingDto.id() != null) {
            if (!jwsHelper.generateSignature(buildingDto.id().toString() + buildingDto.version()).equals(buildingDto.signature())) {
                throw new PreconditionFailedException();
            }
        }
        Building building = new Building(buildingDto.name(), toFloor(buildingDto.floors()));
        building.setVersion(buildingDto.version());
        return building;
    }
}
