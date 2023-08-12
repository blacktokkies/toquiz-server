package blacktokkies.toquiz.domain.member.dto.response;

import blacktokkies.toquiz.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticateResponse {
    private String email;
    private String nickname;
    private LocalDateTime createdAt;
    private String accessToken;

    public static AuthenticateResponse toDto(Member member, String accessToken){
        return AuthenticateResponse.builder()
            .email(member.getEmail())
            .nickname(member.getNickname())
            .createdAt(member.getCreatedDate())
            .accessToken(accessToken)
            .build();
    }
}
