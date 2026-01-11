package pl.lodz.p.it.mateusz.gielczynski.VoteService.repository;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Question;

import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY, readOnly = false)
public interface QuestionRepository extends Repository<Question, UUID> {

    Optional<Question> findById(UUID id);

    void saveAndFlush(Question question);
}
