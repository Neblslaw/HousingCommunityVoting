package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.GroupCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.service.GroupService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.BadRequestException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
class GroupController {

    private final GroupService groupService;

    @GetMapping
    public void getGroups() {

    }

    @GetMapping("{id}/members")
    public void getGroupMembers(UUID id) {

    }

    @GetMapping("/{id}")
    public void getGroupById(@PathVariable String id) {

    }

    @PostMapping
    public void createGroup(GroupCreateDto groupCreateDto) {

    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable String id) {

    }

    @PostMapping("/{groupId}/users")
    public void addUsersToGroup(@PathVariable UUID groupId, @RequestBody List<UUID> usersToAdd) throws NotFoundException, BadRequestException {
        groupService.addUsersToGroup(groupId, usersToAdd);
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public void removeUserFromGroup(@PathVariable String groupId, @PathVariable String userId) {

    }

    @PostMapping("/{groupId}/roles/{roleName}")
    public void addRoleToGroup(@PathVariable String groupId, @PathVariable String roleName) {

    }

    @DeleteMapping("/{groupId}/roles/{roleName}")
    public void removeRoleFromGroup(@PathVariable String groupId, @PathVariable String roleName) {

    }


}
