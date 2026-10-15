package pl.lodz.p.it.mateusz.gielczynski.NotificationService.mapper;

import pl.lodz.p.it.mateusz.gielczynski.NotificationService.dto.NotificationDTO;
import pl.lodz.p.it.mateusz.gielczynski.NotificationService.model.Notification;

import java.util.List;

public class NotificationMapper {

    public static NotificationDTO toDTO(Notification notification) {
        return new NotificationDTO(notification.getId(), notification.getText(), notification.getCreationDate());
    }

    public static List<NotificationDTO> toDTO(List<Notification> notifications) {
        return notifications.stream().map(NotificationMapper::toDTO).toList();
    }
}
