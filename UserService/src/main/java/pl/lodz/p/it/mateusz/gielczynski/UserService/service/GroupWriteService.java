package pl.lodz.p.it.mateusz.gielczynski.UserService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.UserService.consts.i18n;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.GroupCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Role;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.AccountRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.GroupRepository;
import pl.lodz.p.it.mateusz.gielczynski.UserService.repository.RoleRepository;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.BadRequestException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;
import pl.lodz.p.it.mateusz.gielczynski.logger.MethodCallLogged;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@MethodCallLogged
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class GroupWriteService {

    private final GroupRepository groupRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public Group getGroup(String groupName) throws NotFoundException {
        return groupRepository.findGroupByName(groupName).orElseThrow(() -> new NotFoundException(i18n.GROUP_NOT_FOUND));
    }

    public List<Group> getGroups() {
        return groupRepository.findAll();
    }

    public Group createGroup(GroupCreateDto dto) throws BadRequestException {
        try {
            Group group = new Group(dto.name(), new HashSet<>(), 0);
            return groupRepository.saveAndFlush(group);
        } catch (DataIntegrityViolationException exception) {
            throw new BadRequestException(i18n.GROUP_ALREADY_EXISTS);
        }
    }

    public Group updateGroup(Group group) throws NotFoundException, BadRequestException {
        //todo maybe
        return null;
    }

    @Retryable(
            includes = {org.springframework.dao.OptimisticLockingFailureException.class},
            maxRetries = 10
    )
    public void addUsersToGroup(UUID groupId, List<UUID> users) throws NotFoundException {
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

    @Retryable(
            includes = {org.springframework.dao.OptimisticLockingFailureException.class},
            maxRetries = 10
    )
    public void removeUsersFromGroup(UUID groupId, List<UUID> users) throws NotFoundException {
        Group group = groupRepository.getGroupById(groupId).orElseThrow(() -> new NotFoundException(i18n.GROUP_NOT_FOUND));
        int groupsRemoved = 0;

        List<Account> accounts = users.stream().map(id -> accountRepository.findAccountById(id).orElse(null)).toList();
        if (accounts.stream().anyMatch(Objects::isNull)) {
            throw new NotFoundException(i18n.ACCOUNT_NOT_FOUND);
        }
        for (Account account : accounts) {
            if (account.getGroups().contains(group)) {
                account.getGroups().remove(group);
                accountRepository.saveAndFlush(account);
                groupsRemoved++;
            }
        }

        group.setUserCount(group.getUserCount() - groupsRemoved);
        groupRepository.saveAndFlush(group);
    }

    @Retryable(
            includes = {org.springframework.dao.OptimisticLockingFailureException.class},
            maxRetries = 10
    )
    public void addRolesToGroup(UUID groupId, List<UUID> rolesToAdd) throws NotFoundException {
        Group group = groupRepository.getGroupById(groupId).orElseThrow(() -> new NotFoundException(i18n.GROUP_NOT_FOUND));

        List<Role> roles = rolesToAdd.stream().map(id -> roleRepository.findRoleById((id)).orElse(null)).toList();
        if (roles.stream().anyMatch(Objects::isNull)) {
            throw new NotFoundException(i18n.ROLE_NOT_FOUND);
        }
        roles.stream().filter(role -> !group.getRoles().contains(role)).forEach(role -> {
            group.getRoles().add(role);
            role.setGroupsCount(role.getGroupsCount() + 1);
            roleRepository.saveAndFlush(role);
        });
        groupRepository.saveAndFlush(group);
    }

    @Retryable(
            includes = {org.springframework.dao.OptimisticLockingFailureException.class},
            maxRetries = 10
    )
    public void removeRolesFromGroup(UUID groupId, List<UUID> rolesToRemove) throws NotFoundException {
        Group group = groupRepository.getGroupById(groupId).orElseThrow(() -> new NotFoundException(i18n.GROUP_NOT_FOUND));

        List<Role> roles = rolesToRemove.stream().map(id -> roleRepository.findRoleById((id)).orElse(null)).toList();
        if (roles.stream().anyMatch(Objects::isNull)) {
            throw new NotFoundException(i18n.ROLE_NOT_FOUND);
        }
        roles.stream().filter(role -> group.getRoles().contains(role)).forEach(role -> {
            group.getRoles().remove(role);
            role.setGroupsCount(role.getGroupsCount() - 1);
            roleRepository.saveAndFlush(role);
        });
        groupRepository.saveAndFlush(group);
    }


    public void deleteGroup(UUID id) throws NotFoundException, BadRequestException {
        Group group = groupRepository.getGroupById(id).orElseThrow(() -> new NotFoundException(i18n.GROUP_NOT_FOUND));

        if (group.getUserCount() != 0) {
            throw new BadRequestException(i18n.CANNOT_DELETE_GROUP_WITH_USERS);
        }

        groupRepository.deleteGroupById(group.getId());
    }
}
