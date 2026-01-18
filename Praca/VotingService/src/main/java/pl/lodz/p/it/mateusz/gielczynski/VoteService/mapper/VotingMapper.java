package pl.lodz.p.it.mateusz.gielczynski.VoteService.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.VotingDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.PreconditionFailedException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VotingMapper {

    private final JWSHelper jwsHelper;

    public Voting toVoting(VotingDTO dto) throws PreconditionFailedException {
        if (!jwsHelper.generateSignature(dto.id().toString() + dto.version().toString()).equals(dto.signature())) {
            throw new PreconditionFailedException();
        }
        Voting voting = new Voting(
                dto.name(),
                QuestionMapper.toQuestions(dto.questions()),
                dto.owner(),
                dto.plannedStartTime(),
                null, null, null,
                VotingGroupMapper.toVotingGroups(dto.votingGroups())
        );
        voting.setVersion(dto.version());
        return voting;
    }

    public VotingDTO toDto(Voting voting) {
        return new VotingDTO(
                voting.getId(),
                voting.getVersion(),
                voting.getName(),
                QuestionMapper.toDto(voting.getQuestions()),
                VotingGroupMapper.toDto(voting.getVotingGroups()),
                voting.getOwner(),
                voting.getPlannedStartTime(),
                voting.getStartTime(),
                voting.getEndTime(),
                voting.getLockTime(),
                jwsHelper.generateSignature(voting.getId().toString() + voting.getVersion().toString())
        );
    }

    public List<VotingDTO> toDto(List<Voting> votings) {
        return votings.stream().map(this::toDto).toList();
    }
}
