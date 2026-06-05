package pl.lodz.p.it.mateusz.gielczynski.VoteService.integration.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.VotingSession;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.repository.VotingRepository;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TestDbHelper {

    private final VotingRepository votingRepository;

    public Voting saveVoting(Voting voting) {
        return votingRepository.saveAndFlush(voting);
    }

    public Voting saveVoting(VotingSession session) {
        return votingRepository.saveAndFlush(session.getVoting());
    }

    public Optional<Voting> findVoting(UUID id) {
        Optional<Voting> voting = votingRepository.findById(id);
        voting.ifPresent(v -> {
            v.getQuestions().size();
            v.getVotingGroups().size();
        });
        return voting;
    }
}
