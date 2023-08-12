package blacktokkies.toquiz.global.util.auth;

import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.global.common.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static blacktokkies.toquiz.domain.member.exception.MemberErrorCode.INVALID_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class TokenService {
    @Value("${application.security.jwt.access-token.expiration}")
    private Integer ACCESS_TOKEN_EXPIRATION;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private Integer REFRESH_TOKEN_EXPIRATION;

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public String generateRefreshToken(String email){
        Optional<RefreshTokenDetail> optionalRefreshToken = refreshTokenRepository.findByEmail(email);

        // Token이 데이터베이스에 존재하면, 갱신
        if(optionalRefreshToken.isPresent()) {
            RefreshTokenDetail savedRefreshToken = optionalRefreshToken.get();
            savedRefreshToken.setExpiration(REFRESH_TOKEN_EXPIRATION);
            return refreshTokenRepository.save(savedRefreshToken).getRefreshToken();
        }

        // Token이 데이터베이스에 존재하지 않으면, 새로 발급
        String refreshToken = jwtService.generateToken(email, REFRESH_TOKEN_EXPIRATION);

        return refreshTokenRepository.save(RefreshTokenDetail.builder()
            .email(email)
            .refreshToken(refreshToken)
            .expiration(REFRESH_TOKEN_EXPIRATION)
            .build())
            .getRefreshToken();
    }

    public String generateAccessToken(String email){
        return jwtService.generateToken(email, ACCESS_TOKEN_EXPIRATION);
    }

    public void validateRefreshToken(String refreshToken, String email){
        RefreshTokenDetail savedRefreshToken = refreshTokenRepository.findByEmail(email)
            .orElseThrow(() -> new RestApiException(INVALID_REFRESH_TOKEN));

        if(!refreshToken.equals(savedRefreshToken.getRefreshToken())){
            throw new RestApiException(INVALID_REFRESH_TOKEN);
        }
    }

    @Transactional
    public void deleteRefreshToken(String email) {
        refreshTokenRepository.findByEmail(email).ifPresent(
            refreshTokenRepository::delete
        );
    }

    public String getEmail(String token){
        return jwtService.getSubject(token);
    }

    public boolean isTokenValid(String token, Member member){
        return jwtService.isTokenValid(token, member);
    }
}
