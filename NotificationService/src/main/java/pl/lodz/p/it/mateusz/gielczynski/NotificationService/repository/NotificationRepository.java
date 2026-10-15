package pl.lodz.p.it.mateusz.gielczynski.NotificationService.repository;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.model.NOTIFICATION_STATE;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.model.Notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface NotificationRepository extends Repository<Notification, UUID> {

    List<Notification> getAllByState(NOTIFICATION_STATE state);

    void saveAndFlush(Notification notification);

    Optional<Notification> getNotificationById(UUID uuid);
}
