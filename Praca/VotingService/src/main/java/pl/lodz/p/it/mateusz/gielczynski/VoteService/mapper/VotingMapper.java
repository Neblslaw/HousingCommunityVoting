package pl.lodz.p.it.mateusz.gielczynski.VoteService.mapper;

import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.VotingDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;

import java.util.List;

public class VotingMapper {

    public static VotingDTO toDto(Voting voting) {
        return new VotingDTO(
                voting.getId(),
                voting.getVersion(),
                voting.getName(),
                QuestionMapper.toDto(voting.getQuestions()),
                VotingGroupMapper.toDto(voting.getVotingGroups()),
                voting.getOwner(),
                voting.getStartTime()
        );
    }
    //todo signature

    public static List<VotingDTO> toDto(List<Voting> votings) {
        return votings.stream().map(VotingMapper::toDto).toList();
    }

    public static Voting toVoting(VotingDTO dto) {
        Voting voting = new Voting(
                dto.name(),
                QuestionMapper.toQuestions(dto.questions()),
                dto.owner(),
                dto.startTime(),
                VotingGroupMapper.toVotingGroups(dto.votingGroups())
        );
        voting.setVersion(dto.version());
        return voting;
    }

    public static List<Voting> toVotings(List<VotingDTO> dtos) {
        return dtos.stream().map(VotingMapper::toVoting).toList();
    }
}
