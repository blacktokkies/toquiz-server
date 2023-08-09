package blacktokkies.toquiz.auth.dto.response;

import blacktokkies.toquiz.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String email;
    private String nickname;
    private LocalDateTime createdAt;
    private String accessToken;

    public static LoginResponseDto toDto(Member member, String accessToken){
        return LoginResponseDto.builder()
            .email(member.getEmail())
            .nickname(member.getNickname())
            .createdAt(member.getCreatedDate())
            .accessToken(accessToken)
            .build();
    }
}
