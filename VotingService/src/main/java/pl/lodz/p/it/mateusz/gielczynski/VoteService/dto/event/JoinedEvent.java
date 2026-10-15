package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.event;

import java.util.List;
import java.util.UUID;

public record JoinedEvent(
        List<UUID> uuids
) {
}
