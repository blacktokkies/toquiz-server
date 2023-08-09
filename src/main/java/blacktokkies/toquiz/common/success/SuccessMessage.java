package blacktokkies.toquiz.common.success;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SuccessMessage {
    SIGN_UP(HttpStatus.OK.value(), "회원가입에 성공하였습니다."),
    LOG_OUT(HttpStatus.OK.value(), "로그아웃에 성공하였습니다."),
    ;

    private final int statusCode;
    private final String message;
}
