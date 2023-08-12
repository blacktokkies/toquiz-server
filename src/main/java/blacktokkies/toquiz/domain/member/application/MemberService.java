package blacktokkies.toquiz.domain.member.application;

import blacktokkies.toquiz.domain.member.dao.MemberRepository;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.member.dto.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public MemberInfoResponse getMyInfo() {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return MemberInfoResponse.toDto(member);
    }

    public void deleteMyInfo(){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        memberRepository.delete(member);
    }
}
