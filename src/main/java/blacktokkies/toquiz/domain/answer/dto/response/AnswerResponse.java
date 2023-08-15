package blacktokkies.toquiz.domain.answer.dto.response;

import blacktokkies.toquiz.domain.answer.domain.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponse {
    private Long answerId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AnswerResponse toDto(Answer answer){
        return AnswerResponse.builder()
            .answerId(answer.getId())
            .content(answer.getContent())
            .createdAt(answer.getCreatedDate())
            .updatedAt(answer.getUpdatedDate())
            .build();
    }
}
