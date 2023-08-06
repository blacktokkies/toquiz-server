package blacktokkies.toquiz.member.domain;

import blacktokkies.toquiz.common.domain.BaseTime;
import blacktokkies.toquiz.member.dto.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "member_email_unique", columnNames = "email"),
    @UniqueConstraint(name = "member_nickname_unique", columnNames = "nickname")
})
public class Member extends BaseTime {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Builder
    public Member(String email, String password, String nickname, Provider provider) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
    }
}
