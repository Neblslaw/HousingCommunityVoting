package pl.lodz.p.it.mateusz.gielczynski.VoteService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.i18n;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.repository.VotingRepository;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VotingReadService {

    private final VotingRepository votingRepository;


    public Voting getVoting(UUID votingId) throws NotFoundException {
        return votingRepository.findById(votingId).orElseThrow(() -> new NotFoundException(i18n.VOTING_NOT_FOUND));
    }

    public List<Voting> getVotings() {
        return votingRepository.findAll();
    }
}
