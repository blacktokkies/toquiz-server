package blacktokkies.toquiz.helper.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(String email, String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(email, refreshToken));
    }

    @Transactional
    public void delete(String email){
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email).orElseThrow();
        refreshTokenRepository.delete(refreshToken);
    }


}
