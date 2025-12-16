package pl.lodz.p.it.mateusz.gielczynski.VoteService.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VotingRepository extends Repository<Voting, UUID> {
    Voting save(Voting voting);

    Optional<Voting> findById(UUID id);

    List<Voting> findAll();

    void deleteVotingByIdAndLockTime(UUID id, LocalDateTime lockTime);

    List<Voting> getVotingsByOwner(UUID userId);

    @Query(
            value = """
                    SELECT v.*
                    FROM voting v
                    JOIN voting_voting_groups vvg ON vvg.voting_id = v.id
                    JOIN voting_group vg ON vg.id = vvg.voting_groups_id 
                    WHERE :voterId = ANY(vg.voters)
                    """,
            nativeQuery = true
    )
    List<Voting> getVotingsByVotingGroups_Voters(UUID voterId);
}
