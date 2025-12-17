package pl.lodz.p.it.mateusz.gielczynski.EstateService.repository;


import org.springframework.data.repository.Repository;
import pl.lodz.p.it.mateusz.gielczynski.EstateService.model.Building;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BuildingRepository extends Repository<Building, UUID> {

    Optional<Building> findBuildingById(UUID id);

    Building saveAndFlush(Building building);

    List<Building> findAll();

    void deleteBuildingById(UUID uuid);
}
