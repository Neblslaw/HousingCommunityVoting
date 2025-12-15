package pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vote extends AbstractEntity {
    @ManyToOne
    Voting voting;
    @ManyToOne
    Question question;
    @ManyToOne
    VotingGroup group;
    @Column(nullable = false)
    String answer;
}
