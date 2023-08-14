package blacktokkies.toquiz.domain.member.domain;

import blacktokkies.toquiz.domain.panel.domain.Panel;
import blacktokkies.toquiz.global.common.domain.BaseTime;
import blacktokkies.toquiz.domain.model.Provider;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static blacktokkies.toquiz.global.util.auth.PasswordEncryptor.encryptPassword;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "member_email_unique", columnNames = "email"),
    @UniqueConstraint(name = "member_nickname_unique", columnNames = "nickname")
})
public class Member extends BaseTime implements UserDetails{
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

    @OneToMany(mappedBy = "member")
    private List<Panel> panels = new ArrayList<>();

    @Builder
    public Member(String email, String password, String nickname, Provider provider, String activeInfoId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.activeInfoId = activeInfoId;
    }

    public void updateMemberInfo(String password, String nickname){
        this.password = encryptPassword(password);
        this.nickname = nickname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
