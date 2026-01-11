package pl.lodz.p.it.mateusz.gielczynski.VoteService.repository;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.VotingResult;

import java.util.List;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY, readOnly = false)
public interface VotingResultRepository extends Repository<VotingResult, UUID> {

    List<VotingResult> findAllByVoting_Id(UUID votingId);


    void saveAndFlush(VotingResult votingResult);
}
