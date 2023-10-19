package blacktokkies.toquiz.domain.member.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static blacktokkies.toquiz.global.common.validation.ValidationErrorMessage.*;
import static blacktokkies.toquiz.global.common.validation.ValidationFormat.PASSWORD_FORMAT;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMyInfoRequest {
    @Length(min = 8, max = 20, message = PASSWORD_LENGTH_MESSAGE)
    @Pattern(regexp = PASSWORD_FORMAT, message = PASSWORD_FORMAT_MESSAGE)
    private String password;

    @Length(min = 2, max = 20, message = NICKNAME_LENGTH_MESSAGE)
    private String nickname;
}
