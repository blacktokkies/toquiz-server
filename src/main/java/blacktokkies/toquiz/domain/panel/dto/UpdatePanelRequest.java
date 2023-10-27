package blacktokkies.toquiz.domain.panel.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static blacktokkies.toquiz.global.common.validation.ValidationErrorMessage.*;

@Getter
@NoArgsConstructor
public class UpdatePanelRequest {
    @NotBlank(message = TITLE_EMPTY_MESSAGE)
    @Length(min = 3, max = 40, message = TITLE_LENGTH_MESSAGE)
    private String title;

    @Length(max = 50, message = DESCRIPTION_LENGTH_MESSAGE)
    private String description;
}
