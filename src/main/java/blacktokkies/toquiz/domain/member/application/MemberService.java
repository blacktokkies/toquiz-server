package blacktokkies.toquiz.domain.member.application;

import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.member.dto.response.GetMyInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    public GetMyInfoResponse getMyInfo() {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return GetMyInfoResponse.toDto(member);
    }
}
