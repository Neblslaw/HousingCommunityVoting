package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto;

import java.util.List;
import java.util.UUID;


public record VotingResultDTO(
        UUID id,
        List<VotingResultOptionDTO> results,
        Float voteCount,
        UUID question
) {
}
