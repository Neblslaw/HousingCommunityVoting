package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto;

import java.util.List;
import java.util.UUID;

public record VotingGroupDTO(
        String name,
        List<UUID> voters
) {
}
