package blacktokkies.toquiz.domain.question.domain;

import blacktokkies.toquiz.global.common.domain.BaseTime;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false)
    private String activeInfoId;
}
