package blacktokkies.toquiz.domain.question.domain;

import blacktokkies.toquiz.domain.answer.domain.Answer;
import blacktokkies.toquiz.global.common.domain.BaseTime;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
}
