package blacktokkies.toquiz.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static blacktokkies.toquiz.common.validation.ValidationErrorMessage.*;
import static blacktokkies.toquiz.common.validation.ValidationFormat.PASSWORD_FORMAT;

@Getter
@NoArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = EMAIL_EMPTY_MESSAGE)
    @Email(message = EMAIL_FORMAT_MESSAGE)
    private String email;

    @NotBlank(message = PASSWORD_EMPTY_MESSAGE)
    @Length(min = 8, max = 20, message = PASSWORD_LENGTH_MESSAGE)
    @Pattern(regexp = PASSWORD_FORMAT, message = PASSWORD_FORMAT_MESSAGE)
    private String password;
}
