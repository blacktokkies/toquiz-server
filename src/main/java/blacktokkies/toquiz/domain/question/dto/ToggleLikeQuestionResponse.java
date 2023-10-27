package blacktokkies.toquiz.domain.question.dto;

import blacktokkies.toquiz.domain.question.domain.Question;
import lombok.*;

@Getter
@AllArgsConstructor
public class ToggleLikeQuestionResponse {
    private Long id;
    private long likeNum;
    private Boolean isActived;

    public static ToggleLikeQuestionResponse of(Question question, boolean isActived){
            return new ToggleLikeQuestionResponse(
                question.getId(),
                question.getLikeNum(),
                isActived
            );
    }
}
