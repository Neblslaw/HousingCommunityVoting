package pl.lodz.p.it.mateusz.gielczynski.NotificationService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.dto.CreateNotificationDTO;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.model.I18n;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.model.NOTIFICATION_STATE;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.model.Notification;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.repository.NotificationRepository;
import pl.lodz.p.it.mateusz.gielczynski.exceptions.NotFoundException;
import pl.lodz.p.it.mateusz.gielczynski.logger.MethodCallLogged;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@MethodCallLogged
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSSEService notificationSSEService;

    public List<Notification> getActive() {
        return notificationRepository.getAllByState(NOTIFICATION_STATE.CREATED);
    }

    public void create(CreateNotificationDTO dto) {
        Notification notification = new Notification();
        notification.setText(dto.text());
        notificationRepository.saveAndFlush(notification);
        Thread.ofVirtual().start(() -> notificationSSEService.notifyAll(notification.getText()));
    }

    public void archive(UUID id) throws NotFoundException {
        Notification notification = notificationRepository.getNotificationById(id).orElseThrow(() -> new NotFoundException(I18n.NOTIFICATION_NOT_FOUND));
        notification.setState(NOTIFICATION_STATE.ARCHIVED);
    }
}
