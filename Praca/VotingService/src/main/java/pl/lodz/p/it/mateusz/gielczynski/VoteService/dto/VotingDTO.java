package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record VotingDTO(
        UUID id,
        Long version,
        String name,
        List<QuestionDTO> questions,
        List<VotingGroupDTO> votingGroups,
        UUID owner,
        LocalDateTime startTime
) {
}
