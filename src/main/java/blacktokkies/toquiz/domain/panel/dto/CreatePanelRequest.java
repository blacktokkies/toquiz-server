package blacktokkies.toquiz.domain.panel.dto;

import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Optional;

import static blacktokkies.toquiz.global.common.validation.ValidationErrorMessage.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePanelRequest {
    @NotBlank(message = TITLE_EMPTY_MESSAGE)
    @Length(min = 3, max = 40, message = TITLE_LENGTH_MESSAGE)
    private String title;

    @Length(max = 50, message = DESCRIPTION_LENGTH_MESSAGE)
    private String description;
}
