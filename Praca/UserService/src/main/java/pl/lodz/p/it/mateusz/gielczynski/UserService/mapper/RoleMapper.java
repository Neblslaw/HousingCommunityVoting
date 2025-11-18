package pl.lodz.p.it.mateusz.gielczynski.UserService.mapper;

import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.RoleDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Role;

import java.util.List;

public class RoleMapper {

    public static RoleDto toDto(Role role) {
        return new RoleDto(role.getName());
    }

    public static List<RoleDto> toDto(List<Role> roles) {
        return roles.stream().map(RoleMapper::toDto).toList();
    }
}
