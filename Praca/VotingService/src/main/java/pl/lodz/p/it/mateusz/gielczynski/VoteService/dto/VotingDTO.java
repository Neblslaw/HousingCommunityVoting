package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record VotingDTO(
        UUID id,
        Long version,
        String name,
        @NotEmpty @NotNull List<QuestionDTO> questions,
        @NotEmpty @NotNull List<VotingGroupDTO> votingGroups,
        UUID owner,
        @Future LocalDateTime startTime
) {
}
