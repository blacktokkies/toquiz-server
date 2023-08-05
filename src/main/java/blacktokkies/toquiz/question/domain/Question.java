package blacktokkies.toquiz.question.domain;

import blacktokkies.toquiz.common.domain.BaseTime;
import blacktokkies.toquiz.panel.domain.Panel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

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
    private ObjectId toquizMemberId;
}
