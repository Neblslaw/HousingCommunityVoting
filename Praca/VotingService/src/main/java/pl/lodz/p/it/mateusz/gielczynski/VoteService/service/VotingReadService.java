package pl.lodz.p.it.mateusz.gielczynski.VoteService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.i18n;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.VoteAbility;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.VotingResult;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.repository.VoteAbilityRepository;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.repository.VotingRepository;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.repository.VotingResultRepository;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
public class VotingReadService {

    private final VotingRepository votingRepository;
    private final VotingResultRepository votingResultRepository;
    private final VoteAbilityRepository voteAbilityRepository;


    public Voting getVoting(UUID votingId) throws NotFoundException {
        return votingRepository.findById(votingId).orElseThrow(() -> new NotFoundException(i18n.VOTING_NOT_FOUND));
    }

    public List<Voting> getVotings() {
        return votingRepository.findAll();
    }

    public List<Voting> getVotingsOwnedByUser(UUID userId) {
        return votingRepository.getVotingsByOwner(userId);
    }

    public List<Voting> getVotingsByVoter(UUID voterId) {
        return votingRepository.getVotingsByVotingGroups_Voters(voterId);
    }

    public List<VotingResult> getVotingResults(UUID votingId) {
        return votingResultRepository.findAllByVoting_Id(votingId);
    }

    public List<VoteAbility> getVoteAbilitiesByVotingAndUser(UUID votingId, UUID user) {
        return voteAbilityRepository.getVoteAbilitiesByVoting_IdAndUser(votingId, user);
    }

}
