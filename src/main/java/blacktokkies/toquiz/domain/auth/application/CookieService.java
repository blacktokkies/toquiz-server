package blacktokkies.toquiz.domain.auth.application;

import blacktokkies.toquiz.domain.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.domain.activeinfo.domain.ActiveInfo;
import blacktokkies.toquiz.domain.auth.application.RefreshTokenService;
import blacktokkies.toquiz.domain.auth.domain.RefreshTokenDetail;
import blacktokkies.toquiz.domain.auth.domain.RefreshTokenRepository;
import blacktokkies.toquiz.domain.auth.jwt.JwtTokenProvider;
import blacktokkies.toquiz.global.common.error.RestApiException;
import blacktokkies.toquiz.domain.member.domain.MemberRepository;
import blacktokkies.toquiz.domain.member.domain.Member;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static blacktokkies.toquiz.domain.auth.exception.AuthErrorCode.NOT_EXIST_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CookieService {
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;


    @Value("${application.security.cookie.active-info-id.expiration}")
    private Integer ACTIVE_INFO_ID_EXPIRATION;
    @Value("${application.security.cookie.refresh-token.expiration}")
    private Integer REFRESH_TOKEN_EXPIRATION;

    // 로그인 사용자 ActiveInfoId 쿠키 발급
    public Cookie issueActiveInfoIdCookieByEmail(String email){
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_MEMBER));

        String activeInfoId = member.getActiveInfoId();

        Cookie cookie = new Cookie("active_info_id", activeInfoId);
        cookie.setMaxAge(ACTIVE_INFO_ID_EXPIRATION);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }

    @Transactional
    public Cookie issueRefreshTokenCookieByEmail(String email){
        String refreshToken = refreshTokenService.generateRefreshTokenByEmail(email);

        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setMaxAge(REFRESH_TOKEN_EXPIRATION);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }

    public Cookie expireCookie(String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);

        return cookie;
    }
}
