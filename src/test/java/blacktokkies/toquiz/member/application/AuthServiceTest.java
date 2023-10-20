package blacktokkies.toquiz.member.application;

import blacktokkies.toquiz.domain.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.domain.activeinfo.domain.ActiveInfo;
import blacktokkies.toquiz.domain.activeinfo.domain.ActivePanel;
import blacktokkies.toquiz.domain.member.application.AuthService;
import blacktokkies.toquiz.domain.member.dao.MemberRepository;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.member.dto.request.LoginRequest;
import blacktokkies.toquiz.domain.member.dto.request.SignUpRequest;
import blacktokkies.toquiz.domain.member.dto.response.AuthenticateResponse;
import blacktokkies.toquiz.domain.model.Provider;
import blacktokkies.toquiz.global.config.SecurityConfig;
import blacktokkies.toquiz.global.util.auth.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = SecurityConfig.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ActiveInfoRepository activeInfoRepository;
    @Mock
    private TokenService tokenService;

    @Spy
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init(){
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    @DisplayName("회원 가입")
    void signUp(){
        // given
        final SignUpRequest request = SignUpRequest.builder()
            .email("test311@naver.com")
            .password("test@311")
            .nickname("TEST")
            .build();
        final ActiveInfo activeInfo = ActiveInfo.builder()
            .id("5d3f30de25874467999fc7325f6539ba07d62a0b")
            .activePanels(new HashMap<String, ActivePanel>())
            .build();
        Member member = request.toMemberWith(activeInfo);

        doReturn(activeInfo).when(activeInfoRepository).save(any(ActiveInfo.class));
        doReturn(member).when(memberRepository).save(any(Member.class));

        // when
        authService.signUp(request);

        // verify
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(activeInfoRepository, times(1)).save(any(ActiveInfo.class));
    }

    @DisplayName("로그인")
    @Test
    void login(){
        // given
        passwordEncoder = new BCryptPasswordEncoder();

        final String newAccessToken = "{NewAccessToken}";

        LoginRequest request = LoginRequest.builder()
            .email("test311@naver.com")
            .password("test@311")
            .build();

        Optional<Member> optionalMember = Optional.ofNullable(Member.builder()
            .email("test311@naver.com")
            .password(passwordEncoder.encode("test@311"))
            .provider(Provider.LOCAL)
            .nickname("TEST")
            .build());

        doReturn(newAccessToken).when(tokenService).generateAccessToken(any(String.class));
        doReturn(optionalMember).when(memberRepository).findByEmail(any(String.class));

        // when
        AuthenticateResponse result = authService.login(request);

        // then
        assertThat(result.getEmail()).isEqualTo(request.getEmail());
        assertThat(result.getAccessToken()).isEqualTo(newAccessToken);

        // verify
        verify(tokenService, times(1)).generateAccessToken(any(String.class));
    }

    @Test
    @DisplayName("로그아웃")
    void logout(){
        // given
        final Member member = Member.builder()
            .email("test311@naver.com")
            .password(passwordEncoder.encode("test@311"))
            .provider(Provider.LOCAL)
            .nickname("TEST")
            .build();

        doNothing().when(tokenService).deleteRefreshToken(any(String.class));

        // when
        authService.logout(member);

        // verify
        verify(tokenService, times(1)).deleteRefreshToken(any(String.class));
    }

    @Test
    @DisplayName("회원 탈퇴")
    void resign(){
        passwordEncoder = new BCryptPasswordEncoder();
        // given
        final Member member = Member.builder()
            .email("test311@naver.com")
            .password(passwordEncoder.encode("test@311"))
            .provider(Provider.LOCAL)
            .nickname("TEST")
            .activeInfoId("a1c2t3i4v5e6I7n8f9oId")
            .build();
        final String inputPassword = "test@311";
        final String activeInfoId = "{ActiveInfoId}";

        doNothing().when(tokenService).deleteRefreshToken(any(String.class));
        doNothing().when(activeInfoRepository).deleteById(any(String.class));
        doNothing().when(memberRepository).delete(any(Member.class));

        // when
        authService.resign(member, inputPassword, activeInfoId);

        // then
        verify(tokenService, times(1)).deleteRefreshToken(any(String.class));
        verify(activeInfoRepository, times(1)).deleteById(any(String.class));
        verify(memberRepository, times(1)).delete(any(Member.class));
    }

    @Test
    @DisplayName("리프레시 토큰 갱신")
    void refresh(){
        // given
        final String refreshToken = "{RefreshToken}";
        final String newAccessToken = "{NewAccessToken}";
        final Member member = Member.builder()
            .email("test311@naver.com")
            .password(passwordEncoder.encode("test@311"))
            .provider(Provider.LOCAL)
            .nickname("TEST")
            .activeInfoId("a1c2t3i4v5e6I7n8f9oId")
            .build();

        doReturn(true).when(tokenService).isTokenValid(any(String.class), any(Member.class));
        doNothing().when(tokenService).validateRefreshToken(any(String.class), any(String.class));
        doReturn(newAccessToken).when(tokenService).generateAccessToken(any(String.class));

        // when
        AuthenticateResponse result = authService.refresh(member, refreshToken);

        // then
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getAccessToken()).isEqualTo(newAccessToken);

        verify(tokenService, times(1)).isTokenValid(any(String.class), any(Member.class));
        verify(tokenService, times(1)).validateRefreshToken(any(String.class), any(String.class));
        verify(tokenService, times(1)).generateAccessToken(any(String.class));
    }
}
