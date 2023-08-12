package blacktokkies.toquiz.global.common.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    USER_NOT_FOUND(HttpStatus.FORBIDDEN, "User is not exist"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),
    HANDLER_NOT_FOUND(HttpStatus.NOT_FOUND, "요청을 보낸 URI를 처리할 수 있는 핸들러가 없습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
