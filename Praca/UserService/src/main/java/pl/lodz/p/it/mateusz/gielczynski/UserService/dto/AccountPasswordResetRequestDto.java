package pl.lodz.p.it.mateusz.gielczynski.UserService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AccountPasswordResetRequestDto(
        @NotNull @NotBlank @Size(min = 5, max = 32) String email
) {
}
