package pl.lodz.p.it.mateusz.gielczynski.UserService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AccountCreateDto(
        @NotNull @NotBlank @Size(min = 5, max = 15) String login,
        @NotNull @NotBlank @Size(min = 5, max = 32) String email,
        @NotNull @NotBlank @Size(min = 8, max = 64) String password,
        @NotNull @NotBlank @Size(min = 3, max = 25) String firstName,
        @NotNull @NotBlank @Size(min = 3, max = 25) String lastName
) {
}
