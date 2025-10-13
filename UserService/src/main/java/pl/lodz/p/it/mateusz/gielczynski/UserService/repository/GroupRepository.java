package pl.lodz.p.it.mateusz.gielczynski.UserService.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;

import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {
}
