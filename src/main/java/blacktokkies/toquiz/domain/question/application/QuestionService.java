package blacktokkies.toquiz.domain.question.application;

import blacktokkies.toquiz.domain.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.domain.activeinfo.domain.ActiveInfo;
import blacktokkies.toquiz.domain.activeinfo.domain.ActivePanel;
import blacktokkies.toquiz.domain.panel.domain.PanelRepository;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import blacktokkies.toquiz.domain.question.domain.QuestionRepository;
import blacktokkies.toquiz.domain.question.domain.Question;
import blacktokkies.toquiz.domain.question.dto.CreateQuestionRequest;
import blacktokkies.toquiz.domain.question.dto.ModifyQuestionRequest;
import blacktokkies.toquiz.domain.question.dto.GetQuestionsResponse;
import blacktokkies.toquiz.domain.question.dto.QuestionResponse;
import blacktokkies.toquiz.domain.question.dto.ToggleLikeQuestionResponse;
import blacktokkies.toquiz.global.common.annotation.activeInfoId.ActiveInfoIdDto;
import blacktokkies.toquiz.global.common.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static blacktokkies.toquiz.domain.activeinfo.exception.ActiveInfoException.NOT_EXIST_ACTIVE_INFO;
import static blacktokkies.toquiz.domain.panel.exception.PanelErrorCode.NOT_EXIST_PANEL;
import static blacktokkies.toquiz.domain.question.exception.QuestionErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final PanelRepository panelRepository;
    private final ActiveInfoRepository activeInfoRepository;

    @Transactional
    public QuestionResponse createQuestion(
        CreateQuestionRequest createQuestionRequest,
        ActiveInfoIdDto activeInfoIdDto,
        String panelSid
    ) {
        String activeInfoId = activeInfoIdDto.activeInfoId();
        Question question = questionRepository.save(
            new Question(createQuestionRequest.getContent(), getPanel(panelSid), activeInfoId));

        ActiveInfo activeInfo = getActiveInfo(activeInfoId);
        Set<Long> createdQuestionIds = getCreatedQuestions(activeInfo, panelSid);
        createdQuestionIds.add(question.getId());

        activeInfoRepository.save(activeInfo);

        return QuestionResponse.from(question);
    }

    public GetQuestionsResponse getQuestions(String panelSid, Pageable pageable) {
        Page<Question> questions = questionRepository.findAllByPanel(getPanel(panelSid), pageable);
        List<QuestionResponse> questionResponses = questions.stream().map(QuestionResponse::from).toList();
        int nextPage = questions.hasNext() ? pageable.getPageNumber() + 1 : -1;

        return GetQuestionsResponse.of(questionResponses, nextPage);
    }

    @Transactional
    public ToggleLikeQuestionResponse toggleLike(
        Long questionId,
        ActiveInfoIdDto activeInfoIdDto,
         boolean active
    ) {
        Question question = getQuestion(questionId);
        String panelSid = question.getPanel().getSid();

        ActiveInfo activeInfo = getActiveInfo(activeInfoIdDto.activeInfoId());
        Set<Long> likedQuestionIds = getLikedQuestions(activeInfo, panelSid);

        updateLikedQuestions(likedQuestionIds, question, active);

        activeInfoRepository.save(activeInfo);

        return ToggleLikeQuestionResponse.of(question, active);
    }

    @Transactional
    public QuestionResponse modifyQuestion(
        ModifyQuestionRequest modifyQuestionRequest,
        ActiveInfoIdDto activeInfoIdDto,
        Long questionId
    ) {
        Question question = getQuestion(questionId);

        checkMyCreateQuestion(activeInfoIdDto.activeInfoId(), question); // 자신이 생성하지 않은 질문이면 예외처리

        question.updateContent(modifyQuestionRequest.getContent());
        questionRepository.save(question);

        return QuestionResponse.from(question);
    }

    @Transactional
    public void deleteQuestion(Long questionId, ActiveInfoIdDto activeInfoIdDto) {
        Question question = getQuestion(questionId);

        checkMyCreateQuestion(activeInfoIdDto.activeInfoId(), question); // 자신이 생성하지 않은 질문이면 예외처리

        questionRepository.delete(question);
    }

    private void updateLikedQuestions(Set<Long> likedQuestionIds, Question question, boolean active) {
        Long questionId = question.getId();

        boolean isAlreadyLikedQuestion = likedQuestionIds.stream().anyMatch((id) -> Objects.equals(id, questionId));

        if (active) {
            // 좋아요가 이미 활성화된 상태에서 활성화를 하려고 시도할 때 에러처리
            if (isAlreadyLikedQuestion)
                throw new RestApiException(INVALID_ACTIVE_LIKE_QUESTION);
            likedQuestionIds.add(questionId);
            question.increaseLikeNum();
        } else {
            // 좋아요가 이미 비활성화된 상태에서 비활성화를 하려고 시도할 때 에러처리
            if (!isAlreadyLikedQuestion)
                throw new RestApiException(INVALID_INACTIVE_LIKE_QUESTION);
            likedQuestionIds.removeIf((id) -> Objects.equals(id, questionId));
            question.decreaseLikeNum();
        }
    }

    // ------------ [엔티티 가져오는 메서드] ------------ //
    private Panel getPanel(String panelSid) {
        return panelRepository.findBySid(panelSid)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_PANEL));
    }

    private Question getQuestion(Long questionId) {
        return questionRepository.findById(questionId)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_QUESTION));
    }

    private ActiveInfo getActiveInfo(String activeInfoId) {
        return activeInfoRepository.findById(activeInfoId)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_ACTIVE_INFO));
    }

    private Set<Long> getCreatedQuestions(ActiveInfo activeInfo, String panelSid) {
        Map<String, ActivePanel> activePanelMap = activeInfo.getActivePanels();
        if (!activePanelMap.containsKey(panelSid)) {
            activePanelMap.put(panelSid, new ActivePanel());
        }
        ActivePanel activePanel = activePanelMap.get(panelSid);

        return activePanel.getCreatedQuestionIds();
    }

    private Set<Long> getLikedQuestions(ActiveInfo activeInfo, String panelSid) {
        Map<String, ActivePanel> activePanelMap = activeInfo.getActivePanels();
        if (!activePanelMap.containsKey(panelSid)) {
            activePanelMap.put(panelSid, new ActivePanel());
        }
        ActivePanel activePanel = activePanelMap.get(panelSid);

        return activePanel.getLikedQuestionIds();
    }

    // ------------ [검증 메서드] ------------ //
    private void checkMyCreateQuestion(String activeInfoId, Question question){
        String panelSid = question.getPanel().getSid();
        Set<Long> createdQuestions = getCreatedQuestions(getActiveInfo(activeInfoId), panelSid);

        boolean isMyActiveQuestion = createdQuestions.stream().anyMatch(id -> Objects.equals(id, question.getId()));
        if(!isMyActiveQuestion) throw new RestApiException(NOT_MY_CREATE_QUESTION);
    }
}
