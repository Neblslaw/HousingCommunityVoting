package pl.lodz.p.it.mateusz.gielczynski.model;

import java.util.Set;

public record VotingResult(

        float voteCount,
        Set<OptionResult> results

) {

}
