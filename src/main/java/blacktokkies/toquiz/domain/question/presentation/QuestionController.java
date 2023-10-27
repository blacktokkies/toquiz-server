package blacktokkies.toquiz.domain.question.presentation;

import blacktokkies.toquiz.domain.question.application.QuestionService;
import blacktokkies.toquiz.domain.question.dto.CreateQuestionRequest;
import blacktokkies.toquiz.domain.question.dto.ModifyQuestionRequest;
import blacktokkies.toquiz.domain.question.dto.GetQuestionsResponse;
import blacktokkies.toquiz.domain.question.dto.QuestionResponse;
import blacktokkies.toquiz.domain.question.dto.ToggleLikeQuestionResponse;
import blacktokkies.toquiz.global.common.annotation.activeInfoId.ActiveInfoId;
import blacktokkies.toquiz.global.common.annotation.activeInfoId.ActiveInfoIdDto;
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
public class QuestionController {
    private final QuestionService questionService;
    @PostMapping("api/panels/{panelSid}/question")
    public ResponseEntity<SuccessResponse<QuestionResponse>> createQuestion(
        @RequestBody @Valid CreateQuestionRequest request,
        @ActiveInfoId ActiveInfoIdDto activeInfoIdDto,
        @PathVariable String panelSid
    ){
        QuestionResponse response = questionService.createQuestion(request, activeInfoIdDto, panelSid);

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
        @ActiveInfoId ActiveInfoIdDto activeInfoIdDto,
        @RequestParam boolean active
    ){
        ToggleLikeQuestionResponse response = questionService.toggleLike(questionId, activeInfoIdDto, active);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @PatchMapping("api/questions/{questionId}")
    public ResponseEntity<SuccessResponse<QuestionResponse>> modifyQuestion(
        @RequestBody @Valid ModifyQuestionRequest request,
        @PathVariable Long questionId,
        @ActiveInfoId ActiveInfoIdDto activeInfoIdDto
    ){
        QuestionResponse response = questionService.modifyQuestion(request, activeInfoIdDto, questionId);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @DeleteMapping("api/questions/{questionId}")
    public ResponseEntity<SuccessMessage> deleteQuestion(
        @PathVariable Long questionId,
        @ActiveInfoId ActiveInfoIdDto activeInfoIdDto
    ){
        questionService.deleteQuestion(questionId, activeInfoIdDto);

        return ResponseEntity.ok(SuccessMessage.QUESTION_DELETE);
    }
}
