package blacktokkies.toquiz.member.dto;

import blacktokkies.toquiz.member.domain.Member;
import blacktokkies.toquiz.member.domain.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static blacktokkies.toquiz.common.validation.ValidationErrorMessage.*;
import static blacktokkies.toquiz.common.validation.ValidationFormat.MEMBER_PASSWORD_FORMAT;
import static blacktokkies.toquiz.helper.PasswordEncryptor.encryptPassword;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    @NotEmpty(message = EMPTY_MESSAGE)
    @Email(message = MEMBER_EMAIL_MESSAGE)
    private String email;

    @NotEmpty(message = EMPTY_MESSAGE)
    @Length(min = 8, max = 20, message = MEMBER_PASSWORD_LENGTH_MESSAGE)
    @Pattern(regexp = MEMBER_PASSWORD_FORMAT, message = MEMBER_PASSWORD_FORMAT_MESSAGE)
    private String password;

    @NotEmpty(message = EMPTY_MESSAGE)
    @Length(min = 2, max = 20, message = MEMBER_NICKNAME_MESSAGE)
    private String nickname;

    public Member toMember(){
        return Member.builder()
                .email(email)
                .password(encryptPassword(password))
                .nickname(nickname)
                .provider(Provider.LOCAL)
                .build();
    }
}