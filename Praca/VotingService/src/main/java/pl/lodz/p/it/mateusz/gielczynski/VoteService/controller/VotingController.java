package pl.lodz.p.it.mateusz.gielczynski.VoteService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.VotingDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.mapper.VotingMapper;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.service.VotingReadService;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.service.VotingWriteService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.ConflictException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/voting")
@RequiredArgsConstructor
public class VotingController {

    private final VotingReadService votingReadService;
    private final VotingWriteService votingWriteService;

    @GetMapping
    public List<VotingDTO> getVotings() {
        return VotingMapper.toDto(votingReadService.getVotings());
    }

    @GetMapping("/{id}")
    public VotingDTO getVoting(@PathVariable UUID id) throws NotFoundException {
        return VotingMapper.toDto(votingReadService.getVoting(id));
    }

    @GetMapping("/owned/{id}") //todo remove uuid
    public List<VotingDTO> getVotingsByOwner(@PathVariable UUID id) {
        return VotingMapper.toDto(votingReadService.getVotingsOwnedByUser(id));
    }

    @GetMapping("/available/{id}")//todo rename + remove uuid
    public List<VotingDTO> getVotingsByAvailable(@PathVariable UUID id) {
        return VotingMapper.toDto(votingReadService.getVotingsByVoter(id));
    }


    @PostMapping
    public VotingDTO addVoting(@RequestBody VotingDTO voting) {
        return VotingMapper.toDto(votingWriteService.createVoting(VotingMapper.toVoting(voting)));
    }

    @PutMapping
    public VotingDTO updateVoting(@RequestBody VotingDTO voting) throws ConflictException, NotFoundException {
        return VotingMapper.toDto(votingWriteService.updateVoting(voting.id(), VotingMapper.toVoting(voting)));
    }

    @DeleteMapping("/{id}")
    public void deleteVoting(@PathVariable UUID id) {
        votingWriteService.deleteVoting(id);
    }
}
