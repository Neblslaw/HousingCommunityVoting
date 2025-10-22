package pl.lodz.p.it.mateusz.gielczynski.UserService.dto;

import java.util.Set;

public record GroupCreateDto(
        String name,
        Set<String> roles
) {}
