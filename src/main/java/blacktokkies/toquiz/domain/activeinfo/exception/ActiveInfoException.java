package blacktokkies.toquiz.domain.activeinfo.exception;

import blacktokkies.toquiz.global.common.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ActiveInfoException implements ErrorCode {
    NOT_EXIST_ACTIVE_INFO(HttpStatus.NOT_FOUND, "활동 정보가 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
