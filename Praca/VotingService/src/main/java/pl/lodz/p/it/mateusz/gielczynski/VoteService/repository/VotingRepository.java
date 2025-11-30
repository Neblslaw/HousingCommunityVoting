package pl.lodz.p.it.mateusz.gielczynski.VoteService.repository;

import org.springframework.data.repository.Repository;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VotingRepository extends Repository<Voting, UUID> {
    Voting save(Voting voting);

    Optional<Voting> findById(UUID id);

    List<Voting> findAll();

    void deleteById(UUID id);
}
