package blacktokkies.toquiz.domain.member.application;

import blacktokkies.toquiz.global.common.annotation.auth.MemberEmailDto;
import blacktokkies.toquiz.domain.member.domain.MemberRepository;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.member.dto.UpdateMyInfoRequest;
import blacktokkies.toquiz.domain.member.dto.MemberInfoResponse;
import blacktokkies.toquiz.global.common.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static blacktokkies.toquiz.domain.auth.exception.AuthErrorCode.DUPLICATE_NICKNAME;
import static blacktokkies.toquiz.domain.auth.exception.AuthErrorCode.NOT_EXIST_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    public MemberInfoResponse getMyInfo(MemberEmailDto memberEmailDto) {
        Member member = getMemberByEmail(memberEmailDto.email());

        return MemberInfoResponse.from(member);
    }

    @Transactional
    public MemberInfoResponse updateMyInfo(MemberEmailDto memberEmailDto,
                                           UpdateMyInfoRequest updateMyInfoRequest
    ) {
        Member member = getMemberByEmail(memberEmailDto.email());
        updateNickName(member, updateMyInfoRequest.getNickname());
        updatePassword(member, updateMyInfoRequest.getPassword());
        Member updatedMember = memberRepository.save(member);

        return MemberInfoResponse.from(updatedMember);
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

    // ------------ [엔티티 가져오는 메서드] ------------ //
    private Member getMemberByEmail(String email){
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_MEMBER));
    }
}
