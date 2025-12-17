package pl.lodz.p.it.mateusz.gielczynski.EstateService;

import pl.lodz.p.it.mateusz.gielczynski.EstateService.dto.BuildingDto;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.dto.FlatDto;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.dto.FloorDto;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.model.Building;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.model.Flat;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.model.Floor;

import java.util.List;

public class BuildingMapper {

    public static BuildingDto toBuildingDto(Building building) {
        return new BuildingDto(toFloorDto(building.getFloors()), building.getName(), building.getId(), building.getVersion());
    }

    public static List<BuildingDto> toBuildingDto(List<Building> buildings) {
        return buildings.stream().map(BuildingMapper::toBuildingDto).toList();
    }

    public static List<Building> toBuilding(List<BuildingDto> dtos) {
        return dtos.stream().map(BuildingMapper::toBuilding).toList();
    }

    public static Building toBuilding(BuildingDto buildingDto) {
        Building building = new Building(buildingDto.name(), toFloor(buildingDto.floors()));
        building.setVersion(buildingDto.version());
        return building;
    }

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
}
