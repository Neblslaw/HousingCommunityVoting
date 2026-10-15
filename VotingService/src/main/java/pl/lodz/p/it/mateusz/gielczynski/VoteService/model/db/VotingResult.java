package pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotingResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    UUID id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<VotingResultOption> results;
    @Column(nullable = false, updatable = false)
    Float voteCount;
    @OneToOne(fetch = FetchType.EAGER)
    Question question;
    @ManyToOne(fetch = FetchType.EAGER)
    Voting voting;
}
