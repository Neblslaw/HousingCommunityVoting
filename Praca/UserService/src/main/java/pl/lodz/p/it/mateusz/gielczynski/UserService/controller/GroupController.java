package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.GroupCreateDto;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
class GroupController {

    @GetMapping
    public void getGroups() {

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

    @PostMapping("/{groupId}/users/{userId}")
    public void addUserToGroup(@PathVariable String groupId, @PathVariable String userId) {

    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public void removeUserFromGroup(@PathVariable String groupId, @PathVariable String userId){

    }

    @PostMapping("/{groupId}/roles/{roleName}")
    public void addRoleToGroup(@PathVariable String groupId, @PathVariable String roleName) {

    }

    @DeleteMapping("/{groupId}/roles/{roleName}")
    public void removeRoleFromGroup(@PathVariable String groupId, @PathVariable String roleName) {

    }




}
