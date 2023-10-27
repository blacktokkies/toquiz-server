package blacktokkies.toquiz.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetQuestionsResponse {
    private List<QuestionResponse> questions;
    private int nextPage;

    public static GetQuestionsResponse of(List<QuestionResponse> questions, int nextPage){
        return GetQuestionsResponse.builder()
            .questions(questions)
            .nextPage(nextPage)
            .build();
    }
}
