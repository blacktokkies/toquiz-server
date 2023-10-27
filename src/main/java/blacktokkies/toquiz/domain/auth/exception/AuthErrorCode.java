package blacktokkies.toquiz.domain.auth.exception;

import blacktokkies.toquiz.global.common.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 email 입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 nickname 입니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_EXIST_MEMBER(HttpStatus.NOT_FOUND, "아이디가 존재하지 않습니다."),
    NOT_LOGIN_MEMBER(HttpStatus.FORBIDDEN, "로그인된 사용자가 아닙니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
