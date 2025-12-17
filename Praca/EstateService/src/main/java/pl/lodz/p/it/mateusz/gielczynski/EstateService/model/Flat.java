package pl.lodz.p.it.mateusz.gielczynski.EstateService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Flat extends AbstractEntity {
    String number;
    @Column(name = "flat_order")
    int order;
    @Column(columnDefinition = "uuid[]")
    List<UUID> inhabitants;
}
