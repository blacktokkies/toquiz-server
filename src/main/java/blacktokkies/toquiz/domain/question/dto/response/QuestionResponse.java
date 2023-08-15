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
public class QuestionResponse {
    private Long questionId;
    private String content;
    private long answerNum;
    private long likeNum;
    private String authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static QuestionResponse toDto(Question question){
        return QuestionResponse.builder()
            .questionId(question.getId())
            .content(question.getContent())
            .answerNum(0)
            .likeNum(0)
            .authorId(question.getActiveInfoId())
            .createdAt(question.getCreatedDate())
            .updatedAt(question.getUpdatedDate())
            .build();
    }
}
