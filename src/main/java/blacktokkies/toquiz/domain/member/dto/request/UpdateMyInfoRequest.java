package blacktokkies.toquiz.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static blacktokkies.toquiz.global.common.validation.ValidationErrorMessage.*;
import static blacktokkies.toquiz.global.common.validation.ValidationFormat.PASSWORD_FORMAT;

@Getter
@NoArgsConstructor
public class UpdateMyInfoRequest {
    @NotBlank(message = PASSWORD_EMPTY_MESSAGE)
    @Length(min = 8, max = 20, message = PASSWORD_LENGTH_MESSAGE)
    @Pattern(regexp = PASSWORD_FORMAT, message = PASSWORD_FORMAT_MESSAGE)
    private String password;

    @NotBlank(message = NICKNAME_EMPTY_MESSAGE)
    @Length(min = 2, max = 20, message = NICKNAME_LENGTH_MESSAGE)
    private String nickname;
}
