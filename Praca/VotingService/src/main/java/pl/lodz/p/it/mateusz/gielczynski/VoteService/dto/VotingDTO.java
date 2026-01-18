package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record VotingDTO(
        UUID id,
        Long version,
        String name,
        @NotEmpty @NotNull List<QuestionDTO> questions,
        @NotEmpty @NotNull List<VotingGroupDTO> votingGroups,
        @NotNull UUID owner,
        @Future LocalDateTime plannedStartTime,
        @Null LocalDateTime startTime,
        @Null LocalDateTime endTime,
        @Null LocalDateTime lockTime,
        String signature
) {
}
