package blacktokkies.toquiz.common.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidationErrorMessage {
    public static final String MEMBER_PASSWORD_LENGTH_MESSAGE = "비밀번호는 8 ~ 20자 이어야 합니다.";
    public static final String MEMBER_PASSWORD_FORMAT_MESSAGE = "비밀번호는 영문자, 숫자, 특수기호를 반드시 포함해야 합니다.";
    public static final String EMPTY_MESSAGE = "모든 항목을 입력해주세요.";
    public static final String MEMBER_EMAIL_MESSAGE = "아이디는 이메일 형식으로 입력해주세요.";
    public static final String MEMBER_NICKNAME_MESSAGE = "닉네임은 8 ~ 20자 이어야 합니다.";
}
