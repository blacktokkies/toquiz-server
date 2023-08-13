package blacktokkies.toquiz.global.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SuccessMessage {
    SIGN_UP(HttpStatus.OK.value(), "회원가입에 성공하였습니다."),
    LOGOUT(HttpStatus.OK.value(), "로그아웃에 성공하였습니다."),
    RESIGN(HttpStatus.OK.value(), "회원탈퇴에 성공하였습니다."),
    PANEL_DELETE(HttpStatus.OK.value(), "패널 삭제에 성공하였습니다."),
    ;

    private final int statusCode;
    private final String message;
}
