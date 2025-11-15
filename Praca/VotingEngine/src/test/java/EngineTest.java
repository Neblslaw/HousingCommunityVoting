import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.lodz.p.it.mateusz.gielczynski.VoteEngineImpl;
import pl.lodz.p.it.mateusz.gielczynski.VotingEngine;
import pl.lodz.p.it.mateusz.gielczynski.model.OptionResult;
import pl.lodz.p.it.mateusz.gielczynski.model.Vote;
import pl.lodz.p.it.mateusz.gielczynski.model.VotingResult;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

public class EngineTest {


    private static Stream<Arguments> provideVotes() {
        Vote g1o1 = new Vote("group1", "option1");
        Vote g1o2 = new Vote("group1", "option2");
        Vote g2o1 = new Vote("group2", "option1");
        Vote g2o2 = new Vote("group2", "option2");

        return Stream.of(
                argumentSet(
                        "Different votes, different groups",
                        List.of(g1o1, g2o2),
                        new VotingResult(2f,
                                Set.of(
                                        new OptionResult("option1", 1f),
                                        new OptionResult("option2", 1f)
                                )
                        )
                ),
                argumentSet(
                        "Different votes, same group",
                        List.of(g1o1, g1o2),
                        new VotingResult(1f,
                                Set.of(
                                        new OptionResult("option1", 0.5f),
                                        new OptionResult("option2", 0.5f)
                                )
                        )
                ),
                argumentSet(
                        "Different votes same group + vote from different group",
                        List.of(g1o1, g1o2, g2o1),
                        new VotingResult(2f,
                                Set.of(
                                        new OptionResult("option1", 1.5f),
                                        new OptionResult("option2", 0.5f)
                                )
                        )
                ),
                argumentSet(
                        "Same votes, same group",
                        List.of(g1o1, g1o1),
                        new VotingResult(1f,
                                Set.of(
                                        new OptionResult("option1", 1f)
                                )
                        )
                ),
                argumentSet(
                        "Same votes same group + vote from different group",
                        List.of(g1o1, g1o1, g2o2),
                        new VotingResult(2f,
                                Set.of(
                                        new OptionResult("option1", 1f),
                                        new OptionResult("option2", 1f)
                                )
                        )
                ),
                argumentSet(
                        "3 (2+1) votes same group",
                        List.of(g1o1, g1o2, g1o1),
                        new VotingResult(1f,
                                Set.of(
                                        new OptionResult("option1", 0.66f),
                                        new OptionResult("option2", 0.33f)
                                )
                        )
                ),
                argumentSet(
                        "3 (2+1) votes same group + vote from different group",
                        List.of(g1o1, g1o2, g1o1, g2o2),
                        new VotingResult(2f,
                                Set.of(
                                        new OptionResult("option1", 0.66f),
                                        new OptionResult("option2", 1.33f)
                                )
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideVotes")
    public void testVotingEngine(List<Vote> votes, VotingResult expectedResult) {
        VotingEngine engine = new VoteEngineImpl();

        VotingResult result = engine.calculate(votes);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedResult);
    }
}
