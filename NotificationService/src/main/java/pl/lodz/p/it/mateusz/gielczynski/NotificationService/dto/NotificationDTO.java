package pl.lodz.p.it.mateusz.gielczynski.NotificationService.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationDTO(
        UUID id,
        String text,
        LocalDateTime creationDate
) {
}
