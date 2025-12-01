package pl.lodz.p.it.mateusz.gielczynski.VoteService.mapper;

import pl.lodz.p.it.mateusz.gielczynski.VoteService.dto.QuestionDTO;
import pl.lodz.p.it.mateusz.gielczynski.VoteService.model.db.Question;

import java.util.List;

public class QuestionMapper {


    public static List<QuestionDTO> toDto(List<Question> questions) {
        return questions.stream().map(QuestionMapper::toDto).toList();
    }

    public static QuestionDTO toDto(Question question) {
        return new QuestionDTO(question.getQuestion(), question.getAnswers());
    }

    public static Question toQuestion(QuestionDTO questionDTO) {
        return new Question(questionDTO.question(), questionDTO.answers());
    }

    public static List<Question> toQuestions(List<QuestionDTO> dtos) {
        return dtos.stream().map(QuestionMapper::toQuestion).toList();
    }
}
