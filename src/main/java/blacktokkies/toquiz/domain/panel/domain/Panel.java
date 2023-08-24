package blacktokkies.toquiz.domain.panel.domain;

import blacktokkies.toquiz.domain.question.domain.Question;
import blacktokkies.toquiz.global.common.domain.BaseTime;
import blacktokkies.toquiz.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    uniqueConstraints = {@UniqueConstraint(name = "secondary_id_unique", columnNames = "secondary_id")}
)
public class Panel extends BaseTime {
    @Id @GeneratedValue
    @Column(name = "panel_id")
    private Long id;

    @Column(name = "secondary_id", nullable = false)
    private String sid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String description;

    @Column(nullable = false)
    private boolean isArchived;

    @Column(nullable = false)
    private long scarpNum;

    @OneToMany(mappedBy = "panel")
    private List<Question> questions = new ArrayList<>();

    public void updatePanelInfo(String title, String description){
        this.title = title;
        this.description = description;
    }
}
