package pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Voting extends AbstractEntity {

    @Column(nullable = false, length = 300)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @Column(nullable = false)
    private UUID owner;

    @Column(nullable = false)
    private LocalDateTime plannedStartTime;
    private LocalDateTime startTime = null;
    private LocalDateTime endTime = null;
    private LocalDateTime lockTime = null;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VotingGroup> votingGroups;
}
