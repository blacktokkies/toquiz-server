package blacktokkies.toquiz.utils;

import blacktokkies.toquiz.domain.auth.dto.AuthenticateResponse;
import blacktokkies.toquiz.domain.member.domain.Provider;

import java.time.LocalDateTime;

import static blacktokkies.toquiz.utils.Constants.*;

public class Responses {
    public static AuthenticateResponse authenticationResponse() {
        return AuthenticateResponse.builder()
            .id(1L)
            .email(EMAIL)
            .provider(Provider.LOCAL)
            .nickname(NICKNAME)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .accessToken(ACCESS_TOKEN)
            .build();
    }
}
