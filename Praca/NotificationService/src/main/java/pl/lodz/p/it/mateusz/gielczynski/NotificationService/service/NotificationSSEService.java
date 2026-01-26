package pl.lodz.p.it.mateusz.gielczynski.NotificationService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationSSEService {

    private static final long TIMEOUT = 0L; // never timeout automatically

    private final Map<UUID, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(UUID userId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);

        emitters
                .computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>())
                .add(emitter);

        emitter.onCompletion(() -> removeEmitter(userId, emitter));
        emitter.onTimeout(() -> removeEmitter(userId, emitter));
        emitter.onError(e -> removeEmitter(userId, emitter));

        return emitter;
    }

    public void notify(UUID userId, String message) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.forEach(
                    sseEmitter -> {
                        try {
                            sseEmitter.send(message);
                        } catch (IOException e) {
                            log.error("Failed to notify user");
                        }
                    }
            );
        }
    }

    public void notifyAll(String message) {
        emitters.entrySet().stream().flatMap(entry -> {
            return entry.getValue().stream();
        }).forEach(
                sseEmitter -> {
                    try {
                        sseEmitter.send(message);
                    } catch (IOException e) {
                        log.error("Failed to notify user");
                    }
                }
        );
    }

    private void removeEmitter(UUID userId, SseEmitter emitter) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(emitter);
            if (userEmitters.isEmpty()) {
                emitters.remove(userId);
            }
        }
    }

}