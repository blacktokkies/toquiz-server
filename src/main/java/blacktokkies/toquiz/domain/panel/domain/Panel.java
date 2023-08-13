package blacktokkies.toquiz.domain.panel.domain;

import blacktokkies.toquiz.global.common.domain.BaseTime;
import blacktokkies.toquiz.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Panel extends BaseTime {
    @Id @GeneratedValue
    @Column(name = "panel_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String description;

    @Column(nullable = false)
    private boolean isArchived;

    @Column(nullable = false)
    private long scarpNum;
}
