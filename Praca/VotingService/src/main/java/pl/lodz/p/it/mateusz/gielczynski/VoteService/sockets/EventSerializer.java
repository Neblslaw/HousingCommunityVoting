package pl.lodz.p.it.mateusz.gielczynski.VoteService.sockets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventSerializer {

    private final ObjectMapper objectMapper;

    public String serialize(Object event) {
        return objectMapper.writeValueAsString(event);
    }
}
