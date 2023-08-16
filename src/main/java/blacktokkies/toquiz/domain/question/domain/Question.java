package blacktokkies.toquiz.domain.question.domain;

import blacktokkies.toquiz.domain.answer.domain.Answer;
import blacktokkies.toquiz.global.common.domain.BaseTime;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import blacktokkies.toquiz.global.common.error.RestApiException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static blacktokkies.toquiz.domain.question.exception.QuestionErrorCode.INVALID_INACTIVE_LIKE_QUESTION;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question extends BaseTime {
    @Id @GeneratedValue
    @Column(name = "question_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private long answerNum;

    @Column(nullable = false)
    private long likeNum;

    @ManyToOne
    @JoinColumn(name = "panel_id")
    private Panel panel;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    @Column(nullable = false)
    private String activeInfoId;

    public Question(String content, Panel panel, String activeInfoId) {
        this.content = content;
        this.answerNum = 0;
        this.likeNum = 0;
        this.panel = panel;
        this.activeInfoId = activeInfoId;
    }

    public void decreaseLikeNum(){
        if(this.likeNum < 0)
            throw new RestApiException(INVALID_INACTIVE_LIKE_QUESTION);
        this.likeNum -= 1;
    }

    public void increaseLikeNum(){
        this.likeNum += 1;
    }

    public void increaseAnswerNum() {
        this.answerNum += 1;
    }
}
