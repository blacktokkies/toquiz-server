package blacktokkies.toquiz.domain.question.api;

import blacktokkies.toquiz.domain.question.application.QuestionService;
import blacktokkies.toquiz.domain.question.dto.request.CreateQuestionRequest;
import blacktokkies.toquiz.domain.question.dto.response.GetQuestionsResponse;
import blacktokkies.toquiz.domain.question.dto.response.QuestionResponse;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionApi {
    private final QuestionService questionService;
    @PostMapping("api/panels/{panelId}/question")
    public ResponseEntity<SuccessResponse<QuestionResponse>> createQuestion(
        @RequestBody @Valid CreateQuestionRequest request,
        @CookieValue("active_info_id") String activeInfoId,
        @PathVariable Long panelId
    ){
        QuestionResponse response = questionService.createQuestion(request, activeInfoId, panelId);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("api/panels/{panelId}/questions")
    public ResponseEntity<SuccessResponse<GetQuestionsResponse>> getQuestions(
        @PageableDefault(size = 30, sort = {"likeNum", "createdDate"}, direction = Sort.Direction.DESC) Pageable pageable,
        @PathVariable Long panelId
    ){
        List<QuestionResponse> questionResponses = questionService.getQuestions(panelId, pageable);
        GetQuestionsResponse response = GetQuestionsResponse.toDto(questionResponses, pageable);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }
}
