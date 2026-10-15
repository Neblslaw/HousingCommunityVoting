package pl.lodz.p.it.mateusz.gielczynski.VoteService.mapper;

import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.VotingGroupDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.VotingGroup;

import java.util.List;

public class VotingGroupMapper {
    public static List<VotingGroupDTO> toDto(List<VotingGroup> votingGroups) {
        return votingGroups.stream().map(VotingGroupMapper::toDto).toList();
    }

    public static VotingGroupDTO toDto(VotingGroup votingGroup) {
        return new VotingGroupDTO(votingGroup.getName(), votingGroup.getVoters());
    }

    public static VotingGroup toVotingGroup(VotingGroupDTO votingGroup) {
        return new VotingGroup(votingGroup.name(), votingGroup.voters());
    }

    public static List<VotingGroup> toVotingGroups(List<VotingGroupDTO> dtos) {
        return dtos.stream().map(VotingGroupMapper::toVotingGroup).toList();
    }
}
