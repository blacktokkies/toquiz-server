package blacktokkies.toquiz.domain.panel.dto.request;

import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Optional;

import static blacktokkies.toquiz.global.common.validation.ValidationErrorMessage.*;

@Getter
@NoArgsConstructor
public class CreatePanelRequest {
    @NotBlank(message = TITLE_EMPTY_MESSAGE)
    @Length(min = 3, max = 40, message = TITLE_LENGTH_MESSAGE)
    private String title;

    @Length(max = 50, message = DESCRIPTION_LENGTH_MESSAGE)
    private String description;

    public Panel toPanelWith(Member member){
        return Panel.builder()
            .member(member)
            .title(title)
            .description(Optional.ofNullable(description).orElse(""))
            .isArchived(false)
            .scarpNum(0)
            .build();
    }
}
