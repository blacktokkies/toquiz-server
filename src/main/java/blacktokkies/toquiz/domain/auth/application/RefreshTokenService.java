package blacktokkies.toquiz.domain.auth.application;

import blacktokkies.toquiz.domain.auth.domain.RefreshTokenDetail;
import blacktokkies.toquiz.domain.auth.domain.RefreshTokenRepository;
import blacktokkies.toquiz.domain.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${application.security.cookie.refresh-token.expiration}")
    private Integer REFRESH_TOKEN_EXPIRATION;

    @Transactional
    public void deleteRefreshTokenByEmail(String email){
        refreshTokenRepository.findByEmail(email).ifPresent(refreshTokenRepository::delete);
    }

    @Transactional
    public String generateRefreshTokenByEmail(String email){
        Optional<RefreshTokenDetail> optionalRefreshToken = refreshTokenRepository.findByEmail(email);

        // Token이 데이터베이스에 존재하면, 갱신
        if(optionalRefreshToken.isPresent()) {
            RefreshTokenDetail savedRefreshToken = optionalRefreshToken.get();
            savedRefreshToken.setExpiration(REFRESH_TOKEN_EXPIRATION);
            return refreshTokenRepository.save(savedRefreshToken).getRefreshToken();
        }

        // Token이 데이터베이스에 존재하지 않으면, 새로 발급
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        return refreshTokenRepository.save(
            RefreshTokenDetail.of(email, refreshToken, REFRESH_TOKEN_EXPIRATION))
            .getRefreshToken();
    }
}
