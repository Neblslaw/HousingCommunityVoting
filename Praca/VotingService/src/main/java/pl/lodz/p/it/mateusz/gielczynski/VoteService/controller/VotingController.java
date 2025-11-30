package pl.lodz.p.it.mateusz.gielczynski.VoteService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;
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
    public List<Voting> getVotings() {
        return votingReadService.getVotings();
    }

    @GetMapping("/{id}")
    public Voting getVoting(@PathVariable UUID id) throws NotFoundException {
        return votingReadService.getVoting(id);
    }

    @PostMapping
    public Voting addVoting(@RequestBody Voting voting) {
        return votingWriteService.createVoting(voting);
    }

    @PutMapping
    public Voting updateVoting(@RequestBody Voting voting) throws ConflictException, NotFoundException {
        return votingWriteService.updateVoting(voting);
    }

    @DeleteMapping("/{id}")
    public void deleteVoting(@PathVariable UUID id) {
        votingWriteService.deleteVoting(id);
    }
}
