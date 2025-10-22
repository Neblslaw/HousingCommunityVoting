package pl.lodz.p.it.mateusz.gielczynski.UserService.dto;

import java.util.Set;
import java.util.UUID;

public record AccountDetailsDto(
        UUID id,
        String login,
        String email,
        String firstName,
        String lastName,
        boolean archived,
        boolean verified,
        Set<RoleDto> roles,
        Set<GroupDto> groups
) {
}
