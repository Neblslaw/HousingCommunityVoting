package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto;

import java.util.UUID;

public record VoteDTO(
        UUID userId,
        UUID questionId,
        UUID votingId,
        String answer
) {
}
