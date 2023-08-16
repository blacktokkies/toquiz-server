package blacktokkies.toquiz.domain.answer.dto.response;

import blacktokkies.toquiz.domain.question.domain.Question;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAnswersResponse {
    private Long id;
    private String content;
    private long answerNum;
    private long likeNum;
    private String authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AnswerResponse> answers;

    public static GetAnswersResponse toDto(Question question, List<AnswerResponse> answers){
        return GetAnswersResponse.builder()
            .id(question.getId())
            .content(question.getContent())
            .answerNum(question.getAnswerNum())
            .likeNum(question.getLikeNum())
            .authorId(question.getActiveInfoId())
            .createdAt(question.getCreatedDate())
            .updatedAt(question.getUpdatedDate())
            .answers(answers)
            .build();
    }
}
