package pl.lodz.p.it.mateusz.gielczynski.NotificationService.integration.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.model.Notification;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.repository.NotificationRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TestDbHelper {

    private final NotificationRepository notificationRepository;

    public Notification saveNotification(Notification notification) {
        notificationRepository.saveAndFlush(notification);
        return notification;
    }

    public Optional<Notification> findNotification(UUID id) {
        return notificationRepository.getNotificationById(id);
    }

    public List<Notification> findActiveNotifications() {
        return notificationRepository.getAllByState(pl.lodz.p.it.mateusz.gielczynski.NotificationService.model.NOTIFICATION_STATE.CREATED);
    }
}
