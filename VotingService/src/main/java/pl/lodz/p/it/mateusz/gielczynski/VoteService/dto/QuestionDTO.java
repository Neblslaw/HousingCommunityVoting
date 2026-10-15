package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto;

import java.util.List;
import java.util.UUID;

public record QuestionDTO(
        UUID id,
        String question,
        List<String> answers
) {
}
