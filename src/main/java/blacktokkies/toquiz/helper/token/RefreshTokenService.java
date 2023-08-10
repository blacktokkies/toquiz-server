package blacktokkies.toquiz.helper.token;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${application.security.jwt.refresh-token.expiration}")
    private Integer REFRESH_TOKEN_EXPIRATION;

    private final RefreshTokenRepository refreshTokenRepository;

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
