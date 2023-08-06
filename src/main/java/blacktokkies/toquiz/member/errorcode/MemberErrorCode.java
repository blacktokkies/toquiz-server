package blacktokkies.toquiz.member.errorcode;

import blacktokkies.toquiz.common.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 email 입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 nickname 입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
