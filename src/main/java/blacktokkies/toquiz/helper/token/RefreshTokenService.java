package blacktokkies.toquiz.helper.token;

import blacktokkies.toquiz.common.error.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

import static blacktokkies.toquiz.common.error.errorcode.AuthErrorCode.INVALID_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${application.security.jwt.refresh-token.expiration}")
    private Integer REFRESH_TOKEN_EXPIRATION;

    private final RefreshTokenRepository refreshTokenRepository;

    public void validate(String refreshToken, String email){
        RefreshToken savedRefreshToken = refreshTokenRepository.findByEmail(email)
            .orElseThrow(() -> new RestApiException(INVALID_REFRESH_TOKEN));

        if(!Objects.equals(refreshToken, savedRefreshToken.getRefreshToken())){
            throw new RestApiException(INVALID_REFRESH_TOKEN);
        }
    }

    @Transactional
    public void save(String email, String refreshToken) {
        refreshTokenRepository.save(RefreshToken.builder()
                .email(email)
                .refreshToken(refreshToken)
                .expiration(REFRESH_TOKEN_EXPIRATION)
                .build()
        );
    }

    @Transactional
    public void delete(String email){
        refreshTokenRepository.findByEmail(email).ifPresent(
            refreshTokenRepository::delete
        );
    }
}
