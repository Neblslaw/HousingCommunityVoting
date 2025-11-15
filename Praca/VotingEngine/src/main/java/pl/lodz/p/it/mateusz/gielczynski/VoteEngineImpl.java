package pl.lodz.p.it.mateusz.gielczynski;

import pl.lodz.p.it.mateusz.gielczynski.model.OptionResult;
import pl.lodz.p.it.mateusz.gielczynski.model.Vote;
import pl.lodz.p.it.mateusz.gielczynski.model.VotingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VoteEngineImpl implements VotingEngine {

    @Override
    public VotingResult calculate(List<Vote> votes) {
        Map<String, List<String>> groups = new HashMap<>();
        for (Vote vote : votes) {
            List<String> options = groups.getOrDefault(vote.group(), List.of());
            List<String> newOptions = new java.util.ArrayList<>(List.copyOf(options));
            newOptions.add(vote.option());
            groups.put(vote.group(), newOptions);
        }

        Map<String, Float> result = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : groups.entrySet()) {
            for (String option : entry.getValue()) {
                float value = result.getOrDefault(option, 0f) + (float) (Math.floor(1f / entry.getValue().size() * 100) / 100f);
                result.put(option, value);
            }
        }

        return new VotingResult(groups.size(), result.entrySet().stream().map(entry -> new OptionResult(entry.getKey(), entry.getValue())).collect(Collectors.toSet()));
    }
}
