package blacktokkies.toquiz.domain.auth.application;

import blacktokkies.toquiz.domain.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.domain.activeinfo.domain.ActiveInfo;
import blacktokkies.toquiz.domain.activeinfo.domain.ActivePanel;
import blacktokkies.toquiz.domain.auth.jwt.JwtTokenProvider;
import blacktokkies.toquiz.domain.member.domain.MemberRepository;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.auth.dto.LoginRequest;
import blacktokkies.toquiz.domain.auth.dto.SignUpRequest;
import blacktokkies.toquiz.domain.auth.dto.AuthenticateResponse;
import blacktokkies.toquiz.domain.member.domain.Provider;
import blacktokkies.toquiz.global.common.annotation.activeInfoId.ActiveInfoIdDto;
import blacktokkies.toquiz.global.common.annotation.auth.MemberEmailDto;
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

import java.util.HashMap;
import java.util.Optional;

import static blacktokkies.toquiz.utils.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ActiveInfoRepository activeInfoRepository;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

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
            .email(EMAIL)
            .password(PW)
            .nickname(NICKNAME)
            .build();
        final ActiveInfo activeInfo = ActiveInfo.builder()
            .id(ACTIVE_INFO_ID)
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

        LoginRequest request = LoginRequest.builder()
            .email(EMAIL)
            .password(PW)
            .build();

        Optional<Member> optionalMember = Optional.ofNullable(Member.builder()
            .email(EMAIL)
            .password(passwordEncoder.encode(PW))
            .provider(Provider.LOCAL)
            .nickname(NICKNAME)
            .build());

        doReturn(NEW_ACCESS_TOKEN).when(jwtTokenProvider).generateAccessToken(any(String.class));
        doReturn(optionalMember).when(memberRepository).findByEmail(any(String.class));

        // when
        AuthenticateResponse result = authService.login(request);

        // then
        assertThat(result.getEmail()).isEqualTo(request.getEmail());
        assertThat(result.getAccessToken()).isEqualTo(NEW_ACCESS_TOKEN);

        // verify
        verify(jwtTokenProvider, times(1)).generateAccessToken(any(String.class));
    }

    @Test
    @DisplayName("로그아웃")
    void logout(){
        // given
        final MemberEmailDto memberEmailDto = new MemberEmailDto(EMAIL);

        doNothing().when(refreshTokenService).deleteRefreshTokenByEmail(any(String.class));

        // when
        authService.logout(memberEmailDto);

        // then
        verify(refreshTokenService, times(1)).deleteRefreshTokenByEmail(any(String.class));
    }

    @Test
    @DisplayName("회원 탈퇴")
    void resign(){
        passwordEncoder = new BCryptPasswordEncoder();
        // given
        Optional<Member> optionalMember = Optional.ofNullable(Member.builder()
            .email(EMAIL)
            .password(passwordEncoder.encode(PW))
            .provider(Provider.LOCAL)
            .nickname(NICKNAME)
            .build());
        final MemberEmailDto memberEmailDto = new MemberEmailDto(EMAIL);
        final String inputPassword = PW;
        final ActiveInfoIdDto activeInfoIdDto = new ActiveInfoIdDto(ACTIVE_INFO_ID);

        doReturn(optionalMember).when(memberRepository).findByEmail(any(String.class));
        doNothing().when(refreshTokenService).deleteRefreshTokenByEmail(any(String.class));
        doNothing().when(activeInfoRepository).deleteById(any(String.class));
        doNothing().when(memberRepository).delete(any(Member.class));

        // when
        authService.resign(memberEmailDto, inputPassword, activeInfoIdDto);

        // then
        verify(refreshTokenService, times(1)).deleteRefreshTokenByEmail(any(String.class));
        verify(activeInfoRepository, times(1)).deleteById(any(String.class));
        verify(memberRepository, times(1)).delete(any(Member.class));
    }

    @Test
    @DisplayName("리프레시 토큰 갱신")
    void refresh(){
        // given
        Optional<Member> optionalMember = Optional.ofNullable(Member.builder()
            .email(EMAIL)
            .password(passwordEncoder.encode(PW))
            .provider(Provider.LOCAL)
            .nickname(NICKNAME)
            .build());

        doReturn(EMAIL).when(jwtTokenProvider).extractEmailFromRefreshToken(any(String.class));
        doReturn(ACCESS_TOKEN).when(jwtTokenProvider).generateAccessToken(any(String.class));
        doReturn(optionalMember).when(memberRepository).findByEmail(any(String.class));

        // when
        AuthenticateResponse result = authService.refresh(REFRESH_TOKEN);

        // then
        assertThat(result.getEmail()).isEqualTo(EMAIL);
        assertThat(result.getAccessToken()).isEqualTo(ACCESS_TOKEN);

        verify(jwtTokenProvider, times(1))
            .extractEmailFromRefreshToken(any(String.class));
        verify(jwtTokenProvider, times(1))
            .generateAccessToken(any(String.class));
    }
}
