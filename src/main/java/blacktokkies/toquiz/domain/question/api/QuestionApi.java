package blacktokkies.toquiz.domain.question.api;

import blacktokkies.toquiz.domain.question.application.QuestionService;
import blacktokkies.toquiz.domain.question.dto.request.CreateQuestionRequest;
import blacktokkies.toquiz.domain.question.dto.request.ModifyQuestionRequest;
import blacktokkies.toquiz.domain.question.dto.response.GetQuestionsResponse;
import blacktokkies.toquiz.domain.question.dto.response.QuestionResponse;
import blacktokkies.toquiz.domain.question.dto.response.ToggleLikeQuestionResponse;
import blacktokkies.toquiz.global.common.response.SuccessMessage;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class QuestionApi {
    private final QuestionService questionService;
    @PostMapping("api/panels/{panelSid}/question")
    public ResponseEntity<SuccessResponse<QuestionResponse>> createQuestion(
        @RequestBody @Valid CreateQuestionRequest request,
        @CookieValue("active_info_id") String activeInfoId,
        @PathVariable String panelSid
    ){
        QuestionResponse response = questionService.createQuestion(request, activeInfoId, panelSid);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("api/panels/{panelSid}/questions")
    public ResponseEntity<SuccessResponse<GetQuestionsResponse>> getQuestions(
        @PageableDefault(size = 30, sort = {"likeNum", "createdDate"}, direction = Sort.Direction.DESC) Pageable pageable,
        @PathVariable String panelSid
    ){
        GetQuestionsResponse response = questionService.getQuestions(panelSid, pageable);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @PostMapping("api/questions/{questionId}/like")
    public ResponseEntity<SuccessResponse<ToggleLikeQuestionResponse>> likeQuestion(
        @PathVariable Long questionId,
        @CookieValue("active_info_id") String activeInfoId,
        @RequestParam boolean active
    ){
        ToggleLikeQuestionResponse response = questionService.toggleLike(questionId, activeInfoId, active);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @PatchMapping("api/questions/{questionId}")
    public ResponseEntity<SuccessResponse<QuestionResponse>> modifyQuestion(
        @RequestBody @Valid ModifyQuestionRequest request,
        @PathVariable Long questionId,
        @CookieValue("active_info_id") String activeInfoId
    ){
        QuestionResponse response = questionService.modifyQuestion(request, activeInfoId, questionId);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @DeleteMapping("api/questions/{questionId}")
    public ResponseEntity<SuccessMessage> deleteQuestion(
        @PathVariable Long questionId,
        @CookieValue("active_info_id") String activeInfoId
    ){
        questionService.deleteQuestion(questionId, activeInfoId);

        return ResponseEntity.ok(SuccessMessage.QUESTION_DELETE);
    }
}
