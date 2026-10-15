package pl.lodz.p.it.mateusz.gielczynski.NotificationService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String text;
    LocalDateTime creationDate = LocalDateTime.now();
    NOTIFICATION_STATE state = NOTIFICATION_STATE.CREATED;
}



