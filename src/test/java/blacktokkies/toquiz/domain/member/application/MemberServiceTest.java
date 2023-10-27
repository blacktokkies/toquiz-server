package blacktokkies.toquiz.domain.member.application;

import blacktokkies.toquiz.domain.member.application.MemberService;
import blacktokkies.toquiz.domain.member.domain.MemberRepository;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.member.dto.UpdateMyInfoRequest;
import blacktokkies.toquiz.domain.member.dto.MemberInfoResponse;
import blacktokkies.toquiz.domain.member.domain.Provider;
import blacktokkies.toquiz.global.common.annotation.auth.MemberEmailDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static blacktokkies.toquiz.utils.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("자신의 사용자 정보 가져오기")
    void getMyInfo(){}

    @Test
    @DisplayName("자신의 사용자 정보 수정하기")
    void updateMyInfo(){
        // given
        final UpdateMyInfoRequest updateMyInfoRequest = UpdateMyInfoRequest.builder()
            .password(MODIFY_PW)
            .nickname(MODIFY_NICKNAME)
            .build();
        final Optional<Member> optionalMember = Optional.ofNullable(Member.builder()
            .email(EMAIL)
            .password(PW)
            .provider(Provider.LOCAL)
            .nickname(NICKNAME)
            .activeInfoId(ACTIVE_INFO_ID)
            .build());
        final Member member = optionalMember.get();

        final MemberEmailDto memberEmailDto = new MemberEmailDto(EMAIL);

        doReturn(false).when(memberRepository).existsByNickname(any(String.class));
        doReturn(optionalMember).when(memberRepository).findByEmail(any(String.class));
        doReturn(member).when(memberRepository).save(any(Member.class));

        // when
        MemberInfoResponse result = memberService.updateMyInfo(memberEmailDto, updateMyInfoRequest);

        // then
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getNickname()).isEqualTo(MODIFY_NICKNAME);

        verify(memberRepository, times(1)).existsByNickname(any(String.class));
        verify(memberRepository, times(1)).save(any(Member.class));
    }
}
