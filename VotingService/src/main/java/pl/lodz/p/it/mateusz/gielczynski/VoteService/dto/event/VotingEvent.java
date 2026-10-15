package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.event;

import java.util.UUID;

public record VotingEvent(
        UUID votingId,
        VotingEventType type
) {
}
