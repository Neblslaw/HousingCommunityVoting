package pl.lodz.p.it.mateusz.gielczynski.VoteService.model;

import jakarta.websocket.Session;
import pl.lodz.p.it.mateusz.gielczynski.model.Vote;

import java.util.List;
import java.util.UUID;

public record VotingSession(
        List<UUID> usersWhoCanVote,
        List<UUID> voters,
        List<Vote> votes,
        List<Session> sessions
) {
}
