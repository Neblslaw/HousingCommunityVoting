package pl.lodz.p.it.mateusz.gielczynski.VoteService.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.VoteAbility;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.repository.VoteAbilityRepository;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.repository.VoteRepository;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.repository.VotingRepository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class concurrentTest extends IntegrationTestBase {

    private final AtomicInteger count = new AtomicInteger(0);
    private final AtomicInteger nullCount = new AtomicInteger(0);
    private final AtomicInteger exceptionCount = new AtomicInteger(0);
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    VotingRepository votingRepository;
    @Autowired
    VoteAbilityRepository voteAbilityRepository;

    @Test
    void testDelete() throws InterruptedException {

        Voting voting = votingRepository.saveAndFlush(
                new Voting(
                        "test",
                        List.of(),
                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        null,
                        null,
                        null,
                        List.of()
                )
        );

        voteAbilityRepository.saveAndFlush(
                new VoteAbility(
                        voting,
                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        UUID.fromString("00000000-0000-0000-0000-000000000001")
                )
        );


        for (int i = 0; i < 10; i++) {
//            int finalI = i;
            Thread.ofVirtual().start(
                    () -> {
                        try {
//                            Thread.sleep(finalI * 100);
                            if (voteAbilityRepository.deleteByUserAndQuestion(
                                    UUID.fromString("00000000-0000-0000-0000-000000000001"),
                                    UUID.fromString("00000000-0000-0000-0000-000000000001")
                            ) != null) {
                                count.addAndGet(1);
                            } else {
                                nullCount.addAndGet(1);
                            }
                        } catch (ObjectOptimisticLockingFailureException _) {
                            exceptionCount.addAndGet(1);
                        }
//                        catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
                    }
            );
        }
        Thread.sleep(2000);
        System.out.println(count.get() + " " + nullCount.get() + " " + exceptionCount.get());
        if (count.get() != 1) {
            throw new RuntimeException();
        }
    }
}
