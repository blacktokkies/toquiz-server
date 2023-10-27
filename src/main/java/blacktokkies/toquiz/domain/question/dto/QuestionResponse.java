package blacktokkies.toquiz.domain.question.dto;

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
    private Long id;
    private String content;
    private long answerNum;
    private long likeNum;
    private String authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static QuestionResponse from(Question question){
        return QuestionResponse.builder()
            .id(question.getId())
            .content(question.getContent())
            .answerNum(question.getAnswerNum())
            .likeNum(question.getLikeNum())
            .authorId(question.getActiveInfoId())
            .createdAt(question.getCreatedDate())
            .updatedAt(question.getUpdatedDate())
            .build();
    }
}
