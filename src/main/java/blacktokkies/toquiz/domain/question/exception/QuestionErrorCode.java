package blacktokkies.toquiz.domain.question.exception;

import blacktokkies.toquiz.global.common.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QuestionErrorCode implements ErrorCode {
    NOT_EXIST_QUESTION(HttpStatus.NOT_FOUND, "해당 질문이 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
