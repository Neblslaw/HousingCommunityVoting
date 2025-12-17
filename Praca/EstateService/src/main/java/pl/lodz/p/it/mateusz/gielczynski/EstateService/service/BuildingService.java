package pl.lodz.p.it.mateusz.gielczynski.EstateService.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.consts.i18n;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.model.Building;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.repository.BuildingRepository;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.ConflictException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BuildingService {

    private final BuildingRepository buildingRepository;

    public List<Building> getBuildings() {
        return buildingRepository.findAll();
    }

    public Building setBuilding(Building building, UUID id) throws ConflictException {
        Optional<Building> building1 = buildingRepository.findBuildingById(id);
        if (building1.isPresent()) {
            if (!building1.get().getVersion().equals(building.getVersion())) {
                throw new ConflictException(i18n.BUILDING_CONCURRENT_UPDATE);
            }
            Building building2 = building1.get();
            building2.setName(building.getName());
            building2.getFloors().clear();
            building2.getFloors().addAll(building.getFloors());
            return buildingRepository.saveAndFlush(building2); //todo exception
        }
        return buildingRepository.saveAndFlush(building);
    }

    public void deleteBuilding(UUID uuid) {
        buildingRepository.deleteBuildingById(uuid);
    }
}
