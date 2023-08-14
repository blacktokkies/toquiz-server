package blacktokkies.toquiz.domain.member.application;

import blacktokkies.toquiz.domain.member.dao.MemberRepository;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.member.dto.request.UpdateMyInfoRequest;
import blacktokkies.toquiz.domain.member.dto.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    public MemberInfoResponse getMyInfo() {
        return MemberInfoResponse.toDto(getMember());
    }

    @Transactional
    public MemberInfoResponse updateMyInfo(UpdateMyInfoRequest updateMyInfoRequest) {
        Member member = getMember();
        member.updateMemberInfo(
            updateMyInfoRequest.getPassword(),
            updateMyInfoRequest.getNickname()
        );
        Member updatedMember = memberRepository.save(member);

        return MemberInfoResponse.toDto(updatedMember);
    }

    private Member getMember(){
        return (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
