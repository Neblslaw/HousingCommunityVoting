package pl.lodz.p.it.mateusz.gielczynski.VoteService.repository;

import org.springframework.data.repository.Repository;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Vote;

import java.util.List;
import java.util.UUID;

public interface VoteRepository extends Repository<Vote, UUID> {

    List<Vote> findAllByVoting_Id(UUID votingId);

    List<Vote> deleteByVoting_Id(UUID votingId);

    Vote saveAndFlush(Vote vote);
}
