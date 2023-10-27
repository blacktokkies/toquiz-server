package blacktokkies.toquiz.domain.answer.dto;

import blacktokkies.toquiz.domain.answer.domain.Answer;
import blacktokkies.toquiz.domain.question.domain.Question;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static blacktokkies.toquiz.global.common.validation.ValidationErrorMessage.QUESTION_CONTENT_EMPTY_MESSAGE;
import static blacktokkies.toquiz.global.common.validation.ValidationErrorMessage.QUESTION_CONTENT_LENGTH_MESSAGE;

@Getter
@NoArgsConstructor
public class CreateAnswerRequest {
    @NotBlank(message = QUESTION_CONTENT_EMPTY_MESSAGE)
    @Length(max=200, message = QUESTION_CONTENT_LENGTH_MESSAGE)
    private String content;
}
