package pl.lodz.p.it.mateusz.gielczynski.VoteService.dto;

import java.util.List;

public record QuestionDTO(
        String question,
        List<String> answers
) {
}
