package blacktokkies.toquiz.helper;

import blacktokkies.toquiz.common.error.errorcode.AuthErrorCode;
import blacktokkies.toquiz.common.error.exception.RestApiException;
import blacktokkies.toquiz.helper.token.JwtService;
import blacktokkies.toquiz.helper.token.RefreshToken;
import blacktokkies.toquiz.helper.token.RefreshTokenService;
import blacktokkies.toquiz.member.MemberRepository;
import blacktokkies.toquiz.member.domain.Member;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CookieService {
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;
    @Value("${application.security.cookie.active-info-id.expiration}")
    private Integer ACTIVE_INFO_ID_EXPIRATION;
    @Value("${application.security.cookie.refresh-token.expiration}")
    private Integer REFRESH_TOKEN_EXPIRATION;

    public Cookie issueActiveInfoIdCookie(String email){
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new RestApiException(AuthErrorCode.NOT_EXIST_MEMBER));

        String activeInfoId = member.getActiveInfoId();

        Cookie cookie = new Cookie("active_info_id", activeInfoId);
        cookie.setMaxAge(ACTIVE_INFO_ID_EXPIRATION);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }

    @Transactional
    public Cookie issueRefreshTokenCookie(String email){
        RefreshToken refreshToken = refreshTokenService.generate(email);

        Cookie cookie = new Cookie("refresh_token", refreshToken.getRefreshToken());
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
