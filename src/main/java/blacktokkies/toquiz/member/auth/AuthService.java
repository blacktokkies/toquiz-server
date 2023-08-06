package blacktokkies.toquiz.member.auth;

import blacktokkies.toquiz.common.error.exception.RestApiException;
import blacktokkies.toquiz.member.errorcode.MemberErrorCode;
import blacktokkies.toquiz.member.MemberRepository;
import blacktokkies.toquiz.member.domain.Member;
import blacktokkies.toquiz.member.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long signUp(SignUpRequestDto signUpRequestDto){
        checkExistDuplicateEmail(signUpRequestDto.getEmail());
        checkExistDuplicateNickname(signUpRequestDto.getNickname());
        Member member = signUpRequestDto.toMember();
        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    void checkExistDuplicateEmail(String email){
        if(memberRepository.existsByEmail(email)){
            throw new RestApiException(MemberErrorCode.DUPLICATE_EMAIL);
        }
    }

    void checkExistDuplicateNickname(String nickname){
        if(memberRepository.existsByNickname(nickname)){
            throw new RestApiException(MemberErrorCode.DUPLICATE_NICKNAME);
        }
    }
}
