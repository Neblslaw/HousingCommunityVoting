package pl.lodz.p.it.mateusz.gielczynski.NotificationService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.dto.CreateNotificationDTO;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.dto.NotificationDTO;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.mapper.NotificationMapper;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.service.NotificationSSEService;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.service.NotificationService;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;
import pl.lodz.p.it.mateusz.gielczynski.logger.MethodCallLogged;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
@Transactional(propagation = Propagation.NEVER)
@MethodCallLogged
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationSSEService notificationSSEService;

    @GetMapping
    @PreAuthorize("hasRole('notifications:read')")
    public List<NotificationDTO> getActive() {
        return NotificationMapper.toDTO(notificationService.getActive());
    }

    @PostMapping
    @PreAuthorize("hasRole('notifications:create')")
    public void createNotification(@RequestBody @Valid CreateNotificationDTO dto) {
        notificationService.create(dto);
    }

    @PostMapping("{id}/archive")
    @PreAuthorize("hasRole('notifications:archive')")
    public void archiveNotification(@PathVariable UUID id) throws NotFoundException {
        notificationService.archive(id);
    }

    @GetMapping("/subscribe")
    @PreAuthorize("hasRole('notifications:read')")
    public SseEmitter subscribe(@AuthenticationPrincipal Jwt jwt) {
        return notificationSSEService.createEmitter(UUID.fromString(jwt.getClaim("sub")));
    }
}
