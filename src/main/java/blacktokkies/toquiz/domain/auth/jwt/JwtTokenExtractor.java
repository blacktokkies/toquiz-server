package blacktokkies.toquiz.domain.auth.jwt;

import blacktokkies.toquiz.global.common.error.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static blacktokkies.toquiz.domain.auth.exception.AuthErrorCode.*;

@Slf4j
@Component
public class JwtTokenExtractor {

    private static final String PREFIX_BEARER = "Bearer ";

    public String extractAccessToken(final HttpServletRequest request) {
        final String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(PREFIX_BEARER)) {
            return accessToken.substring(PREFIX_BEARER.length());
        }
        log.warn("Access Token 추출 실패 - 올바른 액세스 토큰이 아닙니다.");
        throw new AuthenticationException(INVALID_ACCESS_TOKEN);
    }
}
