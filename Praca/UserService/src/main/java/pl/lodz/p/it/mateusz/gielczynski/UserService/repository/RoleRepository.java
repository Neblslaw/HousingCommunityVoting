package pl.lodz.p.it.mateusz.gielczynski.UserService.repository;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY, readOnly = false)
public interface RoleRepository extends Repository<Role, UUID> {
    Optional<Role> findRoleById(UUID id);

    Role saveAndFlush(Role role);

    void deleteRoleById(UUID id);

    List<Role> findAll();
}
