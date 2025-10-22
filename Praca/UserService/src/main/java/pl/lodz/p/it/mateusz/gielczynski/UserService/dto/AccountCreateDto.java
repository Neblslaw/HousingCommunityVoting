package pl.lodz.p.it.mateusz.gielczynski.UserService.dto;

public record AccountCreateDto(
        String login,
        String email,
        String password,
        String firstName,
        String lastName
) {}
