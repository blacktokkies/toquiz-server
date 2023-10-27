package blacktokkies.toquiz.domain.member.domain;

import blacktokkies.toquiz.domain.panel.domain.Panel;
import blacktokkies.toquiz.global.common.domain.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static blacktokkies.toquiz.domain.auth.util.PasswordEncryptor.encryptPassword;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "member_email_unique", columnNames = "email"),
    @UniqueConstraint(name = "member_nickname_unique", columnNames = "nickname")
})
public class Member extends BaseTime{
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

    @Column(nullable = false)
    private String activeInfoId;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Panel> panels = new ArrayList<>();

    @Builder
    public Member(String email, String password, String nickname, Provider provider, String activeInfoId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.activeInfoId = activeInfoId;
    }

    public void updatePassword(String password){
        this.password = encryptPassword(password);
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }
}
