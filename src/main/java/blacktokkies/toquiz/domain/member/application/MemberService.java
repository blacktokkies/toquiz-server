package blacktokkies.toquiz.domain.member.application;

import blacktokkies.toquiz.domain.member.dao.MemberRepository;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.member.dto.request.UpdateMyInfoRequest;
import blacktokkies.toquiz.domain.member.dto.response.MemberInfoResponse;
import blacktokkies.toquiz.global.common.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static blacktokkies.toquiz.domain.member.exception.MemberErrorCode.DUPLICATE_NICKNAME;

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
        updateNickName(member, updateMyInfoRequest.getNickname());
        updatePassword(member, updateMyInfoRequest.getPassword());
        Member updatedMember = memberRepository.save(member);

        return MemberInfoResponse.toDto(updatedMember);
    }

    private void updateNickName(Member member, String newNickname){
        if(newNickname == null) return;
        if(newNickname.equals(member.getNickname())) return;

        if(memberRepository.existsByNickname(newNickname)){
            throw new RestApiException(DUPLICATE_NICKNAME);
        }
        member.updateNickname(newNickname);
    }

    private void updatePassword(Member member, String newPassword){
        if(newPassword == null) return;

        member.updatePassword(newPassword);
    }

    private Member getMember(){
        return (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
