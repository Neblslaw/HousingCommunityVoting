package pl.lodz.p.it.mateusz.gielczynski.NotificationService.dto;

import jakarta.validation.constraints.NotNull;

public record CreateNotificationDTO(
        @NotNull
        String text
) {
}
