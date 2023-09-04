package blacktokkies.toquiz.domain.answer.domain;

import blacktokkies.toquiz.global.common.domain.BaseTime;
import blacktokkies.toquiz.domain.question.domain.Question;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer extends BaseTime {
    @Id @GeneratedValue
    @Column(name = "answer_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public Answer(String content, Question question){
        this.content = content;
        this.question = question;
    }
}
