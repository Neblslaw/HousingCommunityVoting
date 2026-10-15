package pl.lodz.p.it.mateusz.gielczynski.VoteService.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class VoterState {
    @Getter
    private final UUID userId;
    private final List<UUID> groupsId;
    @Getter
    @Setter
    private WebSocketSession session;
}
