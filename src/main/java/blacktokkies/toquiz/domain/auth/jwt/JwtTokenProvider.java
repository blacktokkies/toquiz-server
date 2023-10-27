package blacktokkies.toquiz.domain.auth.jwt;

import blacktokkies.toquiz.global.common.error.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static blacktokkies.toquiz.domain.auth.exception.AuthErrorCode.INVALID_ACCESS_TOKEN;
import static blacktokkies.toquiz.domain.auth.exception.AuthErrorCode.INVALID_REFRESH_TOKEN;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String EXPIRED_ACCESS_TOKEN_MESSAGE = "EXPIRED_ACCESS_TOKEN";
    private static final String EXPIRED_REFRESH_TOKEN_MESSAGE = "EXPIRED_REFRESH_TOKEN";
    private final String EMAIL_KEY = "email";

    @Value("${application.security.jwt.secret-key}")
    private String jwtAccessTokenSecret;
    @Value("${application.security.jwt.access-token.expiration}")
    private long jwtAccessTokenExpirationInMs;

    @Value("${application.security.jwt.secret-key}")
    private String jwtRefreshTokenSecret;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long jwtRefreshTokenExpirationInMs;

    public String generateAccessToken(final String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtAccessTokenExpirationInMs);
        SecretKey secretKey = new SecretKeySpec(jwtAccessTokenSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
            .claim(EMAIL_KEY, email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey)
            .compact();
    }

    public String extractEmailFromAccessToken(final String token) {
        validateAccessToken(token);
        Jws<Claims> claimsJws = getAccessTokenParser().parseClaimsJws(token);
        String extractedEmail = claimsJws.getBody().get(EMAIL_KEY, String.class);
        if (extractedEmail == null) {
            log.warn("Access Token 추출 실패 - 추출된 이메일이 null입니다.");
            throw new AuthenticationException(INVALID_ACCESS_TOKEN);
        }
        return extractedEmail;
    }

    private JwtParser getAccessTokenParser() {
        return Jwts.parserBuilder()
            .setSigningKey(jwtAccessTokenSecret.getBytes(StandardCharsets.UTF_8))
            .build();
    }

    private void validateAccessToken(final String token) {
        try {
            Claims claims = getAccessTokenParser().parseClaimsJws(token).getBody();
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.warn("Access Token 검증 실패 - 올바른 액세스 토큰이 아닙니다.");
            throw new AuthenticationException(INVALID_ACCESS_TOKEN);
        } catch (ExpiredJwtException e) {
            log.warn("Access Token 검증 실패 - 올바른 액세스 토큰이 아닙니다.");
            throw new ExpiredJwtException(null, null, EXPIRED_ACCESS_TOKEN_MESSAGE);
        }
    }

    public String generateRefreshToken(final String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshTokenExpirationInMs);
        SecretKey secretKey = new SecretKeySpec(jwtRefreshTokenSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
            .claim(EMAIL_KEY, email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey)
            .compact();
    }

    public String extractEmailFromRefreshToken(final String token) {
        validateRefreshToken(token);
        Jws<Claims> claimsJws = getRefreshTokenParser().parseClaimsJws(token);
        String extractedEmail = claimsJws.getBody().get(EMAIL_KEY, String.class);
        if (extractedEmail == null) {
            log.warn("Refresh Token 추출 실패 - 이메일이 null 입니다.");
            throw new AuthenticationException(INVALID_REFRESH_TOKEN);
        }
        return extractedEmail;
    }

    private JwtParser getRefreshTokenParser() {
        return Jwts.parserBuilder()
            .setSigningKey(jwtRefreshTokenSecret.getBytes(StandardCharsets.UTF_8))
            .build();
    }

    private void validateRefreshToken(final String token) {
        try {
            Claims claims = getRefreshTokenParser().parseClaimsJws(token).getBody();
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.warn("Refresh Token 검증 실패 - 올바른 액세스 토큰이 아닙니다.");
            throw new AuthenticationException(INVALID_REFRESH_TOKEN);
        } catch (ExpiredJwtException e) {
            log.warn("Refresh Token 검증 실패 - 만료된 액세스 토큰이 입니다.");
            throw new ExpiredJwtException(null, null, EXPIRED_REFRESH_TOKEN_MESSAGE);
        }
    }
}
