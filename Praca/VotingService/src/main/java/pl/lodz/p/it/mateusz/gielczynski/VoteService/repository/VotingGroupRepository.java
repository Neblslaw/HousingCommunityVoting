package pl.lodz.p.it.mateusz.gielczynski.VoteService.repository;

import org.springframework.data.repository.Repository;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.VotingGroup;

import java.util.List;
import java.util.UUID;

public interface VotingGroupRepository extends Repository<VotingGroup, UUID> {

    List<VotingGroup> findAllByVoters(List<UUID> voters);

}
