package pl.lodz.p.it.mateusz.gielczynski.VoteService.repository;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Vote;

import java.util.List;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY, readOnly = false)
public interface VoteRepository extends Repository<Vote, UUID> {

    List<Vote> findAllByVoting_Id(UUID votingId);

    List<Vote> deleteByVoting_Id(UUID votingId);

    Vote saveAndFlush(Vote vote);
}
