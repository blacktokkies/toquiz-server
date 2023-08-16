package blacktokkies.toquiz.domain.question.application;

import blacktokkies.toquiz.domain.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.domain.activeinfo.domain.ActiveInfo;
import blacktokkies.toquiz.domain.activeinfo.domain.ActivePanel;
import blacktokkies.toquiz.domain.panel.dao.PanelRepository;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import blacktokkies.toquiz.domain.question.dao.QuestionRepository;
import blacktokkies.toquiz.domain.question.domain.Question;
import blacktokkies.toquiz.domain.question.dto.request.CreateQuestionRequest;
import blacktokkies.toquiz.domain.question.dto.response.GetQuestionsResponse;
import blacktokkies.toquiz.domain.question.dto.response.QuestionResponse;
import blacktokkies.toquiz.domain.question.dto.response.ToggleLikeQuestionResponse;
import blacktokkies.toquiz.global.common.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        String activeInfoId,
        Long panelId)
    {
        Question question = questionRepository.save(new Question(createQuestionRequest.getContent(), getPanel(panelId), activeInfoId));

        return QuestionResponse.toDto(question);
    }

    private Panel getPanel(Long panelId){
        return panelRepository.findById(panelId).orElseThrow(
            () -> new RestApiException(NOT_EXIST_PANEL));
    }

    public GetQuestionsResponse getQuestions(Long panelId, Pageable pageable) {
        Page<Question> questions = questionRepository.findAllByPanel(getPanel(panelId), pageable);
        List<QuestionResponse> questionResponses = questions.stream().map(QuestionResponse::toDto).toList();
        int nextPage = questions.hasNext() ? pageable.getPageNumber() + 1 : -1;

        return GetQuestionsResponse.toDto(questionResponses, nextPage);
    }

    @Transactional
    public ToggleLikeQuestionResponse toggleLike(Long questionId, String activeInfoId, boolean active) {
        Question question = getQuestion(questionId);
        Long panelId = question.getPanel().getId();

        ActiveInfo activeInfo = getActiveInfo(activeInfoId);
        List<Long> likedQuestions = getLikedQuestions(activeInfo, panelId);

        updateLikedQuestions(likedQuestions, question, active);

        activeInfoRepository.save(activeInfo);

        return ToggleLikeQuestionResponse.toDto(question, active);
    }

    private void updateLikedQuestions(List<Long> likedQuestions, Question question, boolean active) {
        Long questionId = question.getId();

        boolean isAlreadyLikedQuestion = likedQuestions.stream().anyMatch((id)-> Objects.equals(id, questionId));

        if(active){
            // 좋아요가 이미 활성화된 상태에서 활성화를 하려고 시도할 때 에러처리
            if(isAlreadyLikedQuestion) throw new RestApiException(INVALID_ACTIVE_LIKE_QUESTION);
            likedQuestions.add(questionId);
            question.increaseLike();
        }else{
            // 좋아요가 이미 비활성화된 상태에서 비활성화를 하려고 시도할 때 에러처리
            if(!isAlreadyLikedQuestion) throw new RestApiException(INVALID_INACTIVE_LIKE_QUESTION);
            likedQuestions.removeIf((id)->Objects.equals(id, questionId));
            question.decreaseLike();
        }
    }

    private List<Long> getLikedQuestions(ActiveInfo activeInfo, Long panelId) {
        Map<Long, ActivePanel> activePanelMap = activeInfo.getActivePanels();
        if(!activePanelMap.containsKey(panelId)){
            activePanelMap.put(panelId, new ActivePanel());
        }
        ActivePanel activePanel = activePanelMap.get(panelId);
        return activePanel.getLikedQuestionIds();
    }

    private ActiveInfo getActiveInfo(String activeInfoId) {
        return activeInfoRepository.findById(activeInfoId).orElseThrow(() -> new RestApiException(NOT_EXIST_ACTIVE_INFO));
    }

    private Question getQuestion(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(() -> new RestApiException(NOT_EXIST_QUESTION));
    }
}
