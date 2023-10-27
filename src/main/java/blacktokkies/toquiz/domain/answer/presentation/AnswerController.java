package blacktokkies.toquiz.domain.answer.presentation;

import blacktokkies.toquiz.domain.answer.application.AnswerService;
import blacktokkies.toquiz.domain.answer.dto.CreateAnswerRequest;
import blacktokkies.toquiz.domain.answer.dto.AnswerResponse;
import blacktokkies.toquiz.domain.answer.dto.GetAnswersResponse;
import blacktokkies.toquiz.global.common.annotation.auth.Auth;
import blacktokkies.toquiz.global.common.annotation.auth.MemberEmailDto;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping("api/questions/{questionId}/answer")
    public ResponseEntity<SuccessResponse<AnswerResponse>> createAnswer(
        @Auth MemberEmailDto memberEmailDto,
        @PathVariable Long questionId,
        @RequestBody @Valid CreateAnswerRequest request
    ){
        AnswerResponse response = answerService.createAnswer(memberEmailDto, questionId, request);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("api/questions/{questionId}/answers")
    public ResponseEntity<SuccessResponse<GetAnswersResponse>> getAnswers(
        @PathVariable Long questionId
    ) {
        GetAnswersResponse response = answerService.getAnswers(questionId);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }
}
