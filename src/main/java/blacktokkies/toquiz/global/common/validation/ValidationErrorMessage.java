package blacktokkies.toquiz.global.common.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidationErrorMessage {
    public static final String EMAIL_EMPTY_MESSAGE = "아이디 항목을 입력해주세요.";
    public static final String EMAIL_FORMAT_MESSAGE = "아이디는 이메일 형식으로 입력해주세요.";

    public static final String PASSWORD_EMPTY_MESSAGE = "비밀번호 항목을 입력해주세요.";
    public static final String PASSWORD_LENGTH_MESSAGE = "비밀번호는 8~20자 이어야 합니다.";
    public static final String PASSWORD_FORMAT_MESSAGE = "비밀번호는 영문자, 숫자, 특수기호를 반드시 포함해야 합니다.";

    public static final String NICKNAME_EMPTY_MESSAGE = "닉네임 항목을 입력해주세요.";
    public static final String NICKNAME_LENGTH_MESSAGE = "닉네임은 2~20자 이어야 합니다.";

    public static final String TITLE_EMPTY_MESSAGE = "제목을 입력해주세요.";
    public static final String TITLE_LENGTH_MESSAGE = "제목은 3~40자 이어야 합니다.";

    public static final String DESCRIPTION_LENGTH_MESSAGE = "설명은 50자 이하 이어야 합니다.";

    public static final String QUESTION_CONTENT_EMPTY_MESSAGE = "질문 내용을 입력해주세요.";
    public static final String QUESTION_CONTENT_LENGTH_MESSAGE = "질문은 200자 이하 이어야 합니다.";

    public static final String ANSWER_CONTENT_EMPTY_MESSAGE = "답변 내용을 입력해주세요.";
    public static final String ANSWER_CONTENT_LENGTH_MESSAGE = "답변은 200자 이하 이어야 합니다.";
}
