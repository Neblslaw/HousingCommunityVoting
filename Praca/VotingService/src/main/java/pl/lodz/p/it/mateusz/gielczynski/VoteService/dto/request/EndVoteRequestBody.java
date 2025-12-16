package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.request;

import java.util.UUID;

public record EndVoteRequestBody(
        UUID votingId
) {
}
