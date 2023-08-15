package blacktokkies.toquiz.domain.answer.api;

import blacktokkies.toquiz.domain.answer.application.AnswerService;
import blacktokkies.toquiz.domain.answer.dto.request.CreateAnswerRequest;
import blacktokkies.toquiz.domain.answer.dto.response.AnswerResponse;
import blacktokkies.toquiz.domain.answer.dto.response.GetAnswersResponse;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AnswerApi {
    private final AnswerService answerService;

    @PostMapping("api/questions/{questionId}/answer")
    public ResponseEntity<SuccessResponse<AnswerResponse>> createAnswer(
        @PathVariable Long questionId,
        @RequestBody @Valid CreateAnswerRequest request
    ){
        AnswerResponse response = answerService.createAnswer(questionId, request);

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
