package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.event;

import java.util.UUID;

public record UserEvent(
        UUID votingId,
        UUID userId,
        UserEventType type
) {
}
