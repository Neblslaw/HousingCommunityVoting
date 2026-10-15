package pl.lodz.p.it.mateusz.gielczynski.UserService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.UserService.consts.i18n;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
public class GroupReadService {

    private final GroupRepository groupRepository;
    private final AccountRepository accountRepository;

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public List<Account> findMembers(UUID id) {
        return accountRepository.findAllByGroups_Id(id);
    }

    public Group findById(UUID id) throws NotFoundException {
        return groupRepository.getGroupById(id).orElseThrow(() -> new NotFoundException(i18n.GROUP_NOT_FOUND));
    }
}
