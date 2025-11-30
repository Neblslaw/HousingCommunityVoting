package pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VotingGroup extends AbstractEntity {
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "uuid[]")
    private List<UUID> voters;
}
