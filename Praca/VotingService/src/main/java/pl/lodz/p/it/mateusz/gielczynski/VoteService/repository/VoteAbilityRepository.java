package pl.lodz.p.it.mateusz.gielczynski.VoteService.repository;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.VoteAbility;

import java.util.List;
import java.util.UUID;

@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
public interface VoteAbilityRepository extends Repository<VoteAbility, UUID> {
    VoteAbility deleteByUserAndQuestion(UUID user, UUID question);

    boolean existsByUserAndQuestion(UUID user, UUID question);

    void deleteAllByVotingId(UUID id);

    void saveAndFlush(VoteAbility voteAbility);

    List<VoteAbility> getVoteAbilitiesByVoting_IdAndUser(UUID votingId, UUID user);
}
