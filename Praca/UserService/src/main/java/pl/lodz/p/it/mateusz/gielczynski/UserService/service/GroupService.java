package pl.lodz.p.it.mateusz.gielczynski.UserService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.UserService.consts.i18n;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.BadRequestException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final AccountRepository accountRepository;

    public Group getGroup(String groupName) throws NotFoundException {
        return groupRepository.findGroupByName(groupName).orElseThrow(() -> new NotFoundException(i18n.GROUP_NOT_FOUND));
    }

    public List<Group> getGroups() {
        return groupRepository.findAll();
    }

    public Group createGroup(Group group) throws BadRequestException {
        try {
            return groupRepository.saveAndFlush(group);
        } catch (DataIntegrityViolationException exception) {
            throw new BadRequestException(i18n.GROUP_ALREADY_EXISTS);
        }
    }

    public Group updateGroup(Group group) throws NotFoundException, BadRequestException {
        //todo
        return null;
    }

    @Transactional
    public void addUsersToGroup(UUID groupId, List<UUID> users) throws NotFoundException, BadRequestException {
        Group group = groupRepository.getGroupById(groupId).orElseThrow(() -> new NotFoundException(i18n.GROUP_NOT_FOUND));
        int groupsAdded = 0;

        List<Account> accounts = users.stream().map(id -> accountRepository.findAccountById(id).orElse(null)).toList();
        if (accounts.stream().anyMatch(Objects::isNull)) {
            throw new NotFoundException(i18n.ACCOUNT_NOT_FOUND);
        }
        for (Account account : accounts) {
            if (!account.getGroups().contains(group)) {
                account.getGroups().add(group);
                accountRepository.saveAndFlush(account);
                groupsAdded++;
            }
        }

        group.setUserCount(group.getUserCount() + groupsAdded);
        groupRepository.saveAndFlush(group);
    }

    public void removeUsersFromGroup(UUID groupId, List<UUID> users) throws NotFoundException, BadRequestException {

    }

    public void addRolesToGroup(UUID groupId, List<UUID> roles) throws NotFoundException, BadRequestException {

    }

    public void removeRolesFromGroup(UUID groupId, List<UUID> roles) throws NotFoundException, BadRequestException {

    }


}
