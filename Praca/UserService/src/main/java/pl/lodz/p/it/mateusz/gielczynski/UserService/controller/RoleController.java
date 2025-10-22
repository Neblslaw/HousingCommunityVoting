package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.RoleCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.RoleDto;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
//@MethodCallLogged
class RoleController {


    @PostMapping
    public void createRole(RoleCreateDto roleCreateDto) {

    }

    @DeleteMapping
    public void deleteRole(String roleName) {

    }

    @GetMapping
    public List<RoleDto> getRoles() {
        return List.of();
    }
}
