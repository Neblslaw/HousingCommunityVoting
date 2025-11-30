package pl.lodz.p.it.mateusz.gielczynski.VoteService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.i18n;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.repository.VotingRepository;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.ConflictException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class VotingWriteService {


    private final VotingRepository votingRepository;


    public Voting createVoting(Voting voting) {
        return votingRepository.save(voting); //todo exceptions
    }

    public Voting updateVoting(Voting voting) throws ConflictException, NotFoundException {
        Voting oldVoting = votingRepository.findById(voting.getId()).orElseThrow(() -> new NotFoundException(i18n.VOTING_NOT_FOUND));

        if (!oldVoting.getVersion().equals(voting.getVersion())) {
            throw new ConflictException(i18n.VOTING_CONCURRENT_UPDATE);
        }

        return votingRepository.save(voting);
    }


    public void deleteVoting(UUID id) {
        votingRepository.deleteById(id);
    }
}
