package pl.lodz.p.it.mateusz.gielczynski.VoteService.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Question;
import pl.lodz.p.it.mateusz.gielczynski.model.Vote;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class VoterState {
    @Getter
    private final UUID userId;
    private final UUID groupId;
    private final List<Question> questions;
    private final WebSocketSession webSocketSession;
    private int questionNumber = 0;
    @Getter
    @Setter
    private boolean isConnected = false;

    // todo
    // ogarnąć komunikaty
    // ogarnąć przyjmowanie wartości
    // stan
    // odpowiadać


    @Getter
    @Setter
    private WebSocketSession session;

    public Vote vote(String option) throws InvalidVoteException {
        Question question = questions.get(questionNumber);

        if (question.getAnswers().contains(option)) {
            if (questionNumber + 1 < questions.size()) {
                questionNumber++;
            }
            return new Vote(groupId, option);
        } else {
            throw new InvalidVoteException("400.invalid.vote");
        }
    }

    public Question getCurrentQuestion() {
        return questions.get(questionNumber);
    }
}
