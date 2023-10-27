package blacktokkies.toquiz.domain.panel.domain;

import blacktokkies.toquiz.domain.panel.dto.CreatePanelRequest;
import blacktokkies.toquiz.domain.question.domain.Question;
import blacktokkies.toquiz.global.common.domain.BaseTime;
import blacktokkies.toquiz.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private boolean archived;

    @Column(nullable = false)
    private long scarpNum;

    @OneToMany(mappedBy = "panel", orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public Panel(Member member, CreatePanelRequest createPanelRequest, String sid){
        this.member = member;
        this.title = createPanelRequest.getTitle();
        this.sid = sid;
        this.description = Optional.ofNullable(createPanelRequest.getDescription()).orElse("");
        this.archived = false;
        this.scarpNum = 0;
    }

    public void updatePanelInfo(String title, String description){
        this.title = title;
        this.description = description;
    }
}
