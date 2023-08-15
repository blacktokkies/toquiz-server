package blacktokkies.toquiz.domain.question.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetQuestionsResponse {
    private List<QuestionResponse> questions;
    private int nextPage;

    public static GetQuestionsResponse toDto(List<QuestionResponse> questions, int nextPage){
        return GetQuestionsResponse.builder()
            .questions(questions)
            .nextPage(nextPage)
            .build();
    }
}
