package pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotingResultOption {
    @Id
    UUID id;
    @Column(nullable = false, updatable = false)
    String option;
    @Column(nullable = false, updatable = false)
    Float count;
}
