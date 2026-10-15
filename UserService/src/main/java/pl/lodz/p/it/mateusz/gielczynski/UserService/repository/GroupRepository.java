package pl.lodz.p.it.mateusz.gielczynski.UserService.repository;


import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface GroupRepository extends Repository<Group, UUID> {
    Optional<Group> findGroupByName(String groupName);

    List<Group> findAll();

    Optional<Group> getGroupById(UUID groupId);

    Group saveAndFlush(Group group);

    void deleteAll();

    void deleteGroupById(UUID groupId);
}
