package pl.lodz.p.it.mateusz.gielczynski.VoteService.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal Jwt jwt) {
        return notificationService.createEmitter(UUID.fromString(jwt.getClaim("sub")));
    }
}
