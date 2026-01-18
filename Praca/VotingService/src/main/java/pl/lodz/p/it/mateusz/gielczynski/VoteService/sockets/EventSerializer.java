package pl.lodz.p.it.mateusz.gielczynski.VoteService.sockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.event.UserEvent;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.event.UserVotedEvent;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.event.VotingEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventSerializer {

    private final ObjectMapper objectMapper;

    public String serialize(UserVotedEvent event) {
        return serialize(event);
    }

    public String serialize(VotingEvent event) {
        return serialize(event);
    }

    public String serialize(UserEvent event) {
        return serialize(event);
    }

    public String serialize(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event: {}", event);
            return "";
        }
    }
}
