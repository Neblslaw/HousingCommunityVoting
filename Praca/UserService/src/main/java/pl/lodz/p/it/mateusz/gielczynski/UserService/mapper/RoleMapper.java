package pl.lodz.p.it.mateusz.gielczynski.UserService.mapper;

import pl.lodz.p.it.mateusz.gielczynski.UserService.dto.RoleDto;
import pl.lodz.p.it.mateusz.gielczynski.UserService.model.Role;

public class RoleMapper {

    public static RoleDto toDto(Role role) {
        return new RoleDto(role.getName());
    }
}
