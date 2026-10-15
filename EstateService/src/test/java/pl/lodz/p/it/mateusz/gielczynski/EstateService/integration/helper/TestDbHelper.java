package pl.lodz.p.it.mateusz.gielczynski.EstateService.integration.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.model.Building;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.repository.BuildingRepository;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TestDbHelper {

    private final BuildingRepository buildingRepository;

    public Building saveBuilding(Building building) {
        return buildingRepository.saveAndFlush(building);
    }

    public Optional<Building> findBuilding(UUID id) {
        Optional<Building> building = buildingRepository.findBuildingById(id);
        building.ifPresent(b -> b.getFloors().size());
        return building;
    }

    public Optional<Building> findBuildingByName(String name) {
        return buildingRepository.findAll().stream()
                .filter(b -> b.getName().equals(name))
                .findFirst();
    }
}
