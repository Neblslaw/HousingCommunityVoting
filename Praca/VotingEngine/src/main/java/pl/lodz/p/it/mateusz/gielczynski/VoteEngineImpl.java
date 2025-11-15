package pl.lodz.p.it.mateusz.gielczynski;

import pl.lodz.p.it.mateusz.gielczynski.model.OptionResult;
import pl.lodz.p.it.mateusz.gielczynski.model.Vote;
import pl.lodz.p.it.mateusz.gielczynski.model.VotingResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VoteEngineImpl implements VotingEngine {

    @Override
    public VotingResult calculate(List<Vote> votes) {
        Map<String, List<String>> groups = votes.stream()
                .collect(Collectors.groupingBy(Vote::group,
                        Collectors.mapping(Vote::option, Collectors.toList())));

        return new VotingResult(
                groups.size(),
                groups.values().stream()
                        .flatMap(options -> {
                            float share = (float) (Math.floor(100.0 / options.size()) / 100.0);
                            return options.stream().map(option -> Map.entry(option, share));
                        })
                        .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(Map.Entry::getValue)))
                        .entrySet().stream().map(entry -> new OptionResult(entry.getKey(), entry.getValue().floatValue()))
                        .collect(Collectors.toSet())
        );
    }
}
