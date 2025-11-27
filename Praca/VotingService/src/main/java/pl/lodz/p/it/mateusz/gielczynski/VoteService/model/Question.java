package pl.lodz.p.it.mateusz.gielczynski.VoteService.model;

import java.util.List;

public record Question(
        String question,
        List<String> answers
) {
}
