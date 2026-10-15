package pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteAbility extends AbstractEntity {
    @ManyToOne
    Voting voting;
    @Column(nullable = false, name = "user_id")
    UUID user;
    @Column(nullable = false)
    UUID question;
}
