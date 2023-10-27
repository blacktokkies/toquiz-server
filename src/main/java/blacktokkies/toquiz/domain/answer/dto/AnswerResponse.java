package blacktokkies.toquiz.domain.answer.dto;

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
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AnswerResponse from(Answer answer){
        return AnswerResponse.builder()
            .id(answer.getId())
            .content(answer.getContent())
            .createdAt(answer.getCreatedDate())
            .updatedAt(answer.getUpdatedDate())
            .build();
    }
}
