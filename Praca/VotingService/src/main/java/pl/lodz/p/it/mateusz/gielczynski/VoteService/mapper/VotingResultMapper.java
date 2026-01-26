package pl.lodz.p.it.mateusz.gielczynski.VoteService.mapper;

import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.VotingResultDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.VotingResultOptionDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.VotingResult;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.VotingResultOption;

import java.util.List;

public class VotingResultMapper {

    public static List<VotingResultDTO> toDto(List<VotingResult> votingResults) {
        return votingResults.stream().map(VotingResultMapper::toDto).toList();
    }

    public static VotingResultDTO toDto(VotingResult votingResult) {
        return new VotingResultDTO(
                votingResult.getId(),
                votingResult.getResults().stream().map(VotingResultMapper::toDto).toList(),
                votingResult.getVoteCount(),
                votingResult.getQuestion().getId()
        );
    }

    private static VotingResultOptionDTO toDto(VotingResultOption votingResultOption) {
        return new VotingResultOptionDTO(votingResultOption.getId(), votingResultOption.getOption(), votingResultOption.getCount());
    }
}
