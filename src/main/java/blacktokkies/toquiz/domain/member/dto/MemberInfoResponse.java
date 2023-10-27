package blacktokkies.toquiz.domain.member.dto;

import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.member.domain.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponse {
    private Long id;
    private String email;
    private String nickname;
    private Provider provider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MemberInfoResponse from(Member member){
        return MemberInfoResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .provider(member.getProvider())
            .createdAt(member.getCreatedDate())
            .updatedAt(member.getUpdatedDate())
            .build();
    }
}
