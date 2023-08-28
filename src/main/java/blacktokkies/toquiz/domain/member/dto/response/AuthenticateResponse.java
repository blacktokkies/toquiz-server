package blacktokkies.toquiz.domain.member.dto.response;

import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.model.Provider;
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
    private Long id;
    private String email;
    private String nickname;
    private Provider provider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String accessToken;

    public static AuthenticateResponse toDto(Member member, String accessToken){
        return AuthenticateResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .provider(member.getProvider())
            .nickname(member.getNickname())
            .createdAt(member.getCreatedDate())
            .updatedAt(member.getUpdatedDate())
            .accessToken(accessToken)
            .build();
    }
}
