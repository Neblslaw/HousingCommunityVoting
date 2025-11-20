package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.GroupCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.GroupDetailsDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.GroupDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.mapper.GroupMapper;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Group;
import pl.lodz.p.it.mateusz.gielczynski.UserService.service.GroupReadService;
import pl.lodz.p.it.mateusz.gielczynski.UserService.service.GroupWriteService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.BadRequestException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;
import pl.lodz.p.it.mateusz.gielczynski.logger.MethodCallLogged;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
@MethodCallLogged
class GroupController {

    private final GroupWriteService groupWriteService;
    private final GroupReadService groupReadService;

    @GetMapping
    public List<GroupDto> getGroups() {
        return GroupMapper.toDto(groupReadService.findAll());
    }

//    @GetMapping("{id}/members")
//    public List<AccountDto> getGroupMembers(@PathVariable UUID id) {
//        return groupReadService.findMembers(id);
//    }

    @GetMapping("/{id}")
    public GroupDetailsDto getGroupById(@PathVariable UUID id) throws NotFoundException {
        return GroupMapper.toDetailedDto(groupReadService.findById(id), new HashSet<>(groupReadService.findMembers(id)));
    }

    @PostMapping
    public GroupDetailsDto createGroup(GroupCreateDto groupCreateDto) throws BadRequestException {
        Group group = groupWriteService.createGroup(groupCreateDto);
        return GroupMapper.toDetailedDto(group, new HashSet<>());
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable UUID id) throws NotFoundException, BadRequestException {
        groupWriteService.deleteGroup(id);
    }

    @PostMapping("/{groupId}/users")
    public void addUsersToGroup(@PathVariable UUID groupId, @RequestBody @NotNull @NotEmpty List<UUID> usersToAdd) throws NotFoundException {
        groupWriteService.addUsersToGroup(groupId, usersToAdd);
    }

    @DeleteMapping("/{groupId}/users")
    public void removeUsersFromGroup(@PathVariable UUID groupId, @RequestBody @NotNull @NotEmpty List<UUID> usersToRemove) throws NotFoundException {
        groupWriteService.removeUsersFromGroup(groupId, usersToRemove);
    }

    @PostMapping("/{groupId}/roles")
    public void addRolesToGroup(@PathVariable UUID groupId, @RequestBody @NotNull @NotEmpty List<UUID> rolesToAdd) throws NotFoundException {
        groupWriteService.addRolesToGroup(groupId, rolesToAdd);
    }

    @DeleteMapping("/{groupId}/roles")
    public void removeRolesFromGroup(@PathVariable UUID groupId, @RequestBody @NotNull @NotEmpty List<UUID> rolesToRemove) throws NotFoundException {
        groupWriteService.removeRolesFromGroup(groupId, rolesToRemove);
    }


}
