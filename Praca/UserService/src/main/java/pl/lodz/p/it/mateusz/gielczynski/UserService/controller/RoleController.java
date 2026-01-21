package pl.lodz.p.it.mateusz.gielczynski.UserService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.RoleCreateDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.RoleDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.mapper.RoleMapper;
import pl.lodz.p.it.mateusz.gielczynski.UserService.service.RoleReadService;
import pl.lodz.p.it.mateusz.gielczynski.UserService.service.RoleWriteService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.BadRequestException;
import pl.lodz.p.it.mateusz.gielczynski.logger.MethodCallLogged;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
@MethodCallLogged
class RoleController {

    private final RoleReadService roleReadService;
    private final RoleWriteService roleWriteService;

    @PostMapping
    @PreAuthorize("hasRole('role:create')")
    public RoleDto createRole(RoleCreateDto roleCreateDto) throws BadRequestException {
        return RoleMapper.toDto(roleWriteService.createRole(roleCreateDto.name()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('role:delete')")
    public void deleteRole(UUID id) {
        roleWriteService.deleteRole(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('role:read')")
    public List<RoleDto> getRoles() {
        return RoleMapper.toDto(roleReadService.findAll());
    }
}
