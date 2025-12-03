package pl.lodz.p.it.mateusz.gielczynski.VoteService.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Voting;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class VotingSession {
    private List<VoterState> voters = new ArrayList<>();
    private WebSocketSession ownerSession;
    private Voting voting;
}
