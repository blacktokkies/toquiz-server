package blacktokkies.toquiz.domain.answer.application;

import blacktokkies.toquiz.domain.answer.dao.AnswerRepository;
import blacktokkies.toquiz.domain.answer.domain.Answer;
import blacktokkies.toquiz.domain.answer.dto.request.CreateAnswerRequest;
import blacktokkies.toquiz.domain.answer.dto.response.AnswerResponse;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.question.dao.QuestionRepository;
import blacktokkies.toquiz.domain.question.domain.Question;
import blacktokkies.toquiz.global.common.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static blacktokkies.toquiz.domain.panel.exception.PanelErrorCode.NOT_AUTHORIZED_DELETE;
import static blacktokkies.toquiz.domain.panel.exception.PanelErrorCode.NOT_CREATOR_PANEL;
import static blacktokkies.toquiz.domain.question.exception.QuestionErrorCode.NOT_EXIST_QUESTION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    public AnswerResponse createAnswer(Long questionId, CreateAnswerRequest request) {
        Question question = getQuestion(questionId);
        checkIsAuthorizedToCreate(question);

        Answer answer = answerRepository.save(request.toAnswerWith(question));
        return AnswerResponse.toDto(answer);
    }

    private Member getMember() {
        return (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private Question getQuestion(Long questionId){
        return questionRepository.findById(questionId).orElseThrow(
            ()->new RestApiException(NOT_EXIST_QUESTION)
        );
    }

    private void checkIsAuthorizedToCreate(Question question) {
        Member panelCreator = question.getPanel().getMember();

        Member member = getMember();

        if(!Objects.equals(member.getId(), panelCreator.getId())){
            throw new RestApiException(NOT_CREATOR_PANEL);
        }
    }
}
