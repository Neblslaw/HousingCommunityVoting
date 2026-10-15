package pl.lodz.p.it.mateusz.gielczynski.UserService.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GroupCreateDto(
        @NotNull @NotBlank @Size(min = 4, max = 32) String name
) {
}
