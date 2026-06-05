package pl.lodz.p.it.mateusz.gielczynski.UserService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AccountPasswordChangeDto(
        @NotNull UUID resetCode,
        @NotNull @NotBlank @Size(min = 8, max = 64) String newPassword
) {
}
