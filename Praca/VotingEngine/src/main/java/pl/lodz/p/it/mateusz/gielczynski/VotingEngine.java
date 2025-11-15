package pl.lodz.p.it.mateusz.gielczynski;


import pl.lodz.p.it.mateusz.gielczynski.model.Vote;
import pl.lodz.p.it.mateusz.gielczynski.model.VotingResult;

import java.util.List;

public interface VotingEngine {
    VotingResult calculate(List<Vote> votes);
}
