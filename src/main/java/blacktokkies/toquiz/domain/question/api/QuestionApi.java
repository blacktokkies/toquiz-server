package blacktokkies.toquiz.domain.question.api;

import blacktokkies.toquiz.domain.question.application.QuestionService;
import blacktokkies.toquiz.domain.question.dto.request.CreateQuestionRequest;
import blacktokkies.toquiz.domain.question.dto.response.CreateQuestionResponse;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class QuestionApi {
    private final QuestionService questionService;
    @PostMapping("api/panels/{panelId}/question")
    public ResponseEntity<SuccessResponse<CreateQuestionResponse>> createQuestion(
        @RequestBody @Valid CreateQuestionRequest request,
        @CookieValue("active_info_id") String activeInfoId,
        @PathVariable Long panelId
    ){
        CreateQuestionResponse response = questionService.createQuestion(request, activeInfoId, panelId);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }
}
