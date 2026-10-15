package pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    UUID id;
    @Column(nullable = false, updatable = false)
    String option;
    @Column(nullable = false, updatable = false)
    Float count;
}
