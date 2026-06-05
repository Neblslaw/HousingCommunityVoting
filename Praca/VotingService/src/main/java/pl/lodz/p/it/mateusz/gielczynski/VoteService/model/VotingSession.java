package pl.lodz.p.it.mateusz.gielczynski.VoteService.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class VotingSession {
    private List<VoterState> voters = new CopyOnWriteArrayList<>();
    private WebSocketSession ownerSession;
    private Voting voting;
}
