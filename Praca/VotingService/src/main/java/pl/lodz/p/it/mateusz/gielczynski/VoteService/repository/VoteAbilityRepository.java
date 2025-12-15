package pl.lodz.p.it.mateusz.gielczynski.VoteService.repository;

import org.springframework.data.repository.Repository;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.VoteAbility;

import java.util.UUID;

public interface VoteAbilityRepository extends Repository<VoteAbility, UUID> {
    VoteAbility deleteByUserAndQuestion(UUID user, UUID question);

    boolean existsByUserAndQuestion(UUID user, UUID question);

    void deleteAllByVotingId(UUID id);

    void saveAndFlush(VoteAbility voteAbility);
}
