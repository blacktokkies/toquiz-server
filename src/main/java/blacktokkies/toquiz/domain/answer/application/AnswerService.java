package blacktokkies.toquiz.domain.answer.application;

import blacktokkies.toquiz.domain.answer.domain.AnswerRepository;
import blacktokkies.toquiz.domain.answer.domain.Answer;
import blacktokkies.toquiz.domain.answer.dto.CreateAnswerRequest;
import blacktokkies.toquiz.domain.answer.dto.AnswerResponse;
import blacktokkies.toquiz.domain.answer.dto.GetAnswersResponse;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.member.domain.MemberRepository;
import blacktokkies.toquiz.domain.question.domain.QuestionRepository;
import blacktokkies.toquiz.domain.question.domain.Question;
import blacktokkies.toquiz.global.common.annotation.auth.MemberEmailDto;
import blacktokkies.toquiz.global.common.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static blacktokkies.toquiz.domain.auth.exception.AuthErrorCode.NOT_EXIST_MEMBER;
import static blacktokkies.toquiz.domain.panel.exception.PanelErrorCode.NOT_CREATOR_PANEL;
import static blacktokkies.toquiz.domain.question.exception.QuestionErrorCode.NOT_EXIST_QUESTION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public AnswerResponse createAnswer(
        MemberEmailDto memberEmailDto,
        Long questionId,
        CreateAnswerRequest createAnswerRequest
    ) {
        Question question = getQuestion(questionId);
        checkIsAuthorizedToCreate(getMemberByEmail(memberEmailDto.email()), question);

        Answer answer = new Answer(createAnswerRequest.getContent(), question);
        question.addAnswer(answerRepository.save(answer));

        return AnswerResponse.from(answer);
    }

    public GetAnswersResponse getAnswers(Long questionId) {
        Question question = getQuestion(questionId);
        List<Answer> answers = answerRepository.findAllByQuestionOrderByCreatedDateDesc(question);
        List<AnswerResponse> answerResponses = answers.stream().map(AnswerResponse::from).toList();

        return GetAnswersResponse.of(question, answerResponses);
    }

    // ------------ [엔티티 가져오는 메서드] ------------ //
    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_MEMBER));
    }

    private Question getQuestion(Long questionId){
        return questionRepository.findById(questionId)
            .orElseThrow(()->new RestApiException(NOT_EXIST_QUESTION));
    }

    // ---------------- [검증 메서드] ---------------- //
    private void checkIsAuthorizedToCreate(Member member, Question question) {
        Member panelCreator = question.getPanel().getMember();

        if(!Objects.equals(member.getId(), panelCreator.getId())){
            throw new RestApiException(NOT_CREATOR_PANEL);
        }
    }
}
