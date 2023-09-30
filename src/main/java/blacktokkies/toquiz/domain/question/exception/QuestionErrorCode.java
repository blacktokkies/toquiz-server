package blacktokkies.toquiz.domain.question.exception;

import blacktokkies.toquiz.global.common.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QuestionErrorCode implements ErrorCode {
    NOT_EXIST_QUESTION(HttpStatus.NOT_FOUND, "해당 질문이 존재하지 않습니다."),
    INVALID_ACTIVE_LIKE_QUESTION(HttpStatus.BAD_REQUEST, "유효하지 않은 좋아요 활성화 요청입니다."),
    INVALID_INACTIVE_LIKE_QUESTION(HttpStatus.BAD_REQUEST, "유효하지 않은 좋아요 비활성화 요청입니다"),
    NOT_ACTIVE_QUESTION(HttpStatus.BAD_REQUEST, "사용자가 활동하지 않은 질문에 대한 잘못된 요청입니다");
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
