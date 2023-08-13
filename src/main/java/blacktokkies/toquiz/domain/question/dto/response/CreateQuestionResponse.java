package blacktokkies.toquiz.domain.question.dto.response;

import blacktokkies.toquiz.domain.question.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionResponse {
    private Long questionId;
    private String content;
    private long answerNum;
    private long likeNum;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CreateQuestionResponse toDto(Question question){
        return CreateQuestionResponse.builder()
            .questionId(question.getId())
            .content(question.getContent())
            .answerNum(0)
            .likeNum(0)
            .createdAt(question.getCreatedDate())
            .updatedAt(question.getUpdatedDate())
            .build();
    }
}
