package blacktokkies.toquiz.domain.question.application;

import blacktokkies.toquiz.domain.panel.dao.PanelRepository;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import blacktokkies.toquiz.domain.question.dao.QuestionRepository;
import blacktokkies.toquiz.domain.question.domain.Question;
import blacktokkies.toquiz.domain.question.dto.request.CreateQuestionRequest;
import blacktokkies.toquiz.domain.question.dto.response.CreateQuestionResponse;
import blacktokkies.toquiz.global.common.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static blacktokkies.toquiz.domain.panel.exception.PanelErrorCode.NOT_EXIST_PANEL;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final PanelRepository panelRepository;

    @Transactional
    public CreateQuestionResponse createQuestion(
        CreateQuestionRequest createQuestionRequest,
        String activeInfoId,
        Long panelId)
    {
        Question question = questionRepository.save(new Question(createQuestionRequest.getContent(), getPanel(panelId), activeInfoId));

        return CreateQuestionResponse.toDto(question);
    }

    private Panel getPanel(Long panelId){
        return panelRepository.findById(panelId).orElseThrow(
            () -> new RestApiException(NOT_EXIST_PANEL));
    }
}
