package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.GroupCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Account;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;
import pl.lodz.p.it.mateusz.gielczynski.UserService.service.GroupReadService;
import pl.lodz.p.it.mateusz.gielczynski.UserService.service.GroupWriteService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.BadRequestException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
class GroupController {

    private final GroupWriteService groupWriteService;
    private final GroupReadService groupReadService;

    @GetMapping
    public List<Group> getGroups() {
        return groupReadService.findAll();
    }

    @GetMapping("{id}/members")
    public List<Account> getGroupMembers(@PathVariable UUID id) {
        return groupReadService.findMembers(id);
    }

    @GetMapping("/{id}")
    public Group getGroupById(@PathVariable UUID id) throws NotFoundException {
        return groupReadService.findById(id);
    }

    @PostMapping
    public Group createGroup(GroupCreateDto groupCreateDto) throws BadRequestException {
        return groupWriteService.createGroup(groupCreateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable UUID id) throws NotFoundException, BadRequestException {
        groupWriteService.deleteGroup(id);
    }

    @PostMapping("/{groupId}/users")
    public void addUsersToGroup(@PathVariable UUID groupId, @RequestBody List<UUID> usersToAdd) throws NotFoundException {
        groupWriteService.addUsersToGroup(groupId, usersToAdd);
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public void removeUsersFromGroup(@PathVariable UUID groupId, @RequestBody List<UUID> usersToRemove) throws NotFoundException {
        groupWriteService.removeUsersFromGroup(groupId, usersToRemove);
    }

    @PostMapping("/{groupId}/roles")
    public void addRolesToGroup(@PathVariable UUID groupId, @RequestBody List<UUID> rolesToAdd) throws NotFoundException {
        groupWriteService.addRolesToGroup(groupId, rolesToAdd);
    }

    @DeleteMapping("/{groupId}/roles")
    public void removeRolesFromGroup(@PathVariable UUID groupId, @RequestBody List<UUID> rolesToRemove) throws NotFoundException {
        groupWriteService.removeRolesFromGroup(groupId, rolesToRemove);
    }


}
