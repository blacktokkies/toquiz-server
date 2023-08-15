package blacktokkies.toquiz.domain.panel.exception;

import blacktokkies.toquiz.global.common.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PanelErrorCode implements ErrorCode {
    NOT_EXIST_PANEL(HttpStatus.NOT_FOUND, "해당 패널이 존재하지 않습니다."),
    NOT_AUTHORIZED_DELETE(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다."),
    NOT_CREATOR_PANEL(HttpStatus.FORBIDDEN, "패널 생성자가 아닙니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
