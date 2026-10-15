package pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question extends AbstractEntity {
    private String question;
    private List<String> answers;
}
