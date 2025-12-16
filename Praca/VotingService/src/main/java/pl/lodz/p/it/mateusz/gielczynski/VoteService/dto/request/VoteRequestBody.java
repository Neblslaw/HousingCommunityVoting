package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.request;

import java.util.UUID;

public record VoteRequestBody(
        UUID votingId,
        UUID questionId,
        String answer
) {
}
