package blacktokkies.toquiz.domain.answer.domain;

import blacktokkies.toquiz.global.common.domain.BaseTime;
import blacktokkies.toquiz.domain.question.domain.Question;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseTime {
    @Id @GeneratedValue
    @Column(name = "answer_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
