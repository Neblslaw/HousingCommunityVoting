package pl.lodz.p.it.mateusz.gielczynski.VoteService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.VotingDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.mapper.VotingMapper;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.service.VotingReadService;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.service.VotingWriteService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.ConflictException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.PreconditionFailedException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/voting")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class VotingController {

    private final VotingReadService votingReadService;
    private final VotingWriteService votingWriteService;
    private final VotingMapper votingMapper;

    @GetMapping
    @PreAuthorize("hasRole('votings:readAll')")
    public List<VotingDTO> getVotings() {
        return votingMapper.toDto(votingReadService.getVotings());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('votings:read')")
    public VotingDTO getVoting(@PathVariable UUID id) throws NotFoundException {
        return votingMapper.toDto(votingReadService.getVoting(id));
    }

    @GetMapping("/owned")
    @PreAuthorize("hasRole('votings:manage')")
    public List<VotingDTO> getVotingsByOwner(@AuthenticationPrincipal Jwt jwt) {
        return votingMapper.toDto(votingReadService.getVotingsOwnedByUser(UUID.fromString(jwt.getClaim("sub"))));
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('votings:participate')")
    public List<VotingDTO> getVotingsByAvailable(@AuthenticationPrincipal Jwt jwt) {
        return votingMapper.toDto(votingReadService.getVotingsByVoter(UUID.fromString(jwt.getClaim("sub"))));
    }

    @PostMapping
    @PreAuthorize("hasRole('votings:manage')")
    public VotingDTO addVoting(@RequestBody @Valid VotingDTO voting) throws PreconditionFailedException {
        return votingMapper.toDto(votingWriteService.createVoting(votingMapper.toVoting(voting)));
    }

    @PutMapping
    @PreAuthorize("hasRole('votings:manage')")
    public VotingDTO updateVoting(@RequestBody @Valid VotingDTO voting) throws ConflictException, NotFoundException, PreconditionFailedException {
        return votingMapper.toDto(votingWriteService.updateVoting(voting.id(), votingMapper.toVoting(voting)));
    }

    @PostMapping("/{id}/lock")
    @PreAuthorize("hasRole('votings:manage')")
    public void lockVoting(@PathVariable UUID id) throws ConflictException, NotFoundException {
        votingWriteService.lockVoting(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('votings:manage')")
    public void deleteVoting(@PathVariable UUID id) throws ConflictException, NotFoundException {
        votingWriteService.deleteVoting(id);
    }
}
