package pl.lodz.p.it.mateusz.gielczynski.UserService.dto;

import java.util.Set;
import java.util.UUID;

public record GroupDetailsDto(
        UUID id,
        String name,
        Set<AccountDto> members,
        Set<RoleDto> roles
) {
}
