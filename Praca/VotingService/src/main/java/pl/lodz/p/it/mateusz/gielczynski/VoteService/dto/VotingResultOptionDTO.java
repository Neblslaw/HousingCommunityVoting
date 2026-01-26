package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto;

import java.util.UUID;


public record VotingResultOptionDTO(
        UUID id,
        String option,
        Float count
) {
}
