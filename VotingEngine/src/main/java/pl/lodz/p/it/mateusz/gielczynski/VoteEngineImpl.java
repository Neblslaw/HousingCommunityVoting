package pl.lodz.p.it.mateusz.gielczynski;

import pl.lodz.p.it.mateusz.gielczynski.model.OptionResult;
import pl.lodz.p.it.mateusz.gielczynski.model.Vote;
import pl.lodz.p.it.mateusz.gielczynski.model.VotingResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VoteEngineImpl implements VotingEngine {

    private static Collector<Map.Entry<String, Float>, ?, Map<String, Double>> sumByOption() {
        return Collectors.groupingBy(Map.Entry::getKey,
                Collectors.summingDouble(e -> e.getValue().doubleValue()));
    }

    private static Stream<Map.Entry<String, Float>> toOptionWithWeight(List<String> options) {
        float share = (float) (Math.floor(100.0 / options.size()) / 100.0);
        return options.stream().map(option -> Map.entry(option, share));
    }

    private static OptionResult toOptionResult(Map.Entry<String, Double> entry) {
        return new OptionResult(entry.getKey(), entry.getValue().floatValue());
    }

    private static Collector<Vote, ?, Map<String, List<String>>> groupByVoteGroup() {
        return Collectors.groupingBy(Vote::group, Collectors.mapping(Vote::option, Collectors.toList()));
    }

    @Override
    public VotingResult calculate(List<Vote> votes) {
        Map<String, List<String>> groups = votes.stream().collect(groupByVoteGroup());

        return new VotingResult(
                groups.size(),
                groups.values().stream()
                        .flatMap(VoteEngineImpl::toOptionWithWeight)
                        .collect(sumByOption())
                        .entrySet().stream().map(VoteEngineImpl::toOptionResult)
                        .collect(Collectors.toSet())
        );
    }
}
