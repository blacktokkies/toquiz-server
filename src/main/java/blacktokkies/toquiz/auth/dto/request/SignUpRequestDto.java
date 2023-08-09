package blacktokkies.toquiz.auth.dto.request;

import blacktokkies.toquiz.member.domain.Member;
import blacktokkies.toquiz.member.domain.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static blacktokkies.toquiz.common.validation.ValidationErrorMessage.*;
import static blacktokkies.toquiz.common.validation.ValidationFormat.PASSWORD_FORMAT;
import static blacktokkies.toquiz.helper.PasswordEncryptor.encryptPassword;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    @NotBlank(message = EMAIL_EMPTY_MESSAGE)
    @Email(message = EMAIL_FORMAT_MESSAGE)
    private String email;

    @NotBlank(message = PASSWORD_EMPTY_MESSAGE)
    @Length(min = 8, max = 20, message = PASSWORD_LENGTH_MESSAGE)
    @Pattern(regexp = PASSWORD_FORMAT, message = PASSWORD_FORMAT_MESSAGE)
    private String password;

    @NotBlank(message = NICKNAME_EMPTY_MESSAGE)
    @Length(min = 2, max = 20, message = NICKNAME_LENGTH_MESSAGE)
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