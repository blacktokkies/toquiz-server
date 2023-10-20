package blacktokkies.toquiz.member.application;

import blacktokkies.toquiz.domain.member.application.MemberService;
import blacktokkies.toquiz.domain.member.dao.MemberRepository;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.member.dto.request.UpdateMyInfoRequest;
import blacktokkies.toquiz.domain.member.dto.response.MemberInfoResponse;
import blacktokkies.toquiz.domain.model.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        final String updatePassword = "update@311";
        final String updateNickname = "update";
        final UpdateMyInfoRequest updateMyInfoRequest = UpdateMyInfoRequest.builder()
            .password(updatePassword)
            .nickname(updateNickname)
            .build();
        final Member member = Member.builder()
            .email("test311@naver.com")
            .password("test@311")
            .provider(Provider.LOCAL)
            .nickname("TEST")
            .activeInfoId("a1c2t3i4v5e6I7n8f9oId")
            .build();

        doReturn(false).when(memberRepository).existsByNickname(any(String.class));
        doReturn(member).when(memberRepository).save(any(Member.class));

        // when
        MemberInfoResponse result = memberService.updateMyInfo(member, updateMyInfoRequest);

        // then
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getNickname()).isEqualTo(updateNickname);

        verify(memberRepository, times(1)).existsByNickname(any(String.class));
        verify(memberRepository, times(1)).save(any(Member.class));
    }
}
