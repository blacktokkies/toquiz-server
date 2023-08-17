package blacktokkies.toquiz.domain.member.application;

import blacktokkies.toquiz.domain.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.domain.activeinfo.domain.ActiveInfo;
import blacktokkies.toquiz.domain.member.dto.response.AuthenticateResponse;
import blacktokkies.toquiz.global.common.error.RestApiException;
import blacktokkies.toquiz.domain.member.dto.request.LoginRequest;
import blacktokkies.toquiz.domain.member.dto.request.SignUpRequest;
import blacktokkies.toquiz.domain.member.dao.MemberRepository;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.global.util.auth.PasswordEncryptor;
import blacktokkies.toquiz.global.util.auth.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static blacktokkies.toquiz.domain.member.exception.MemberErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final ActiveInfoRepository activeInfoRepository;
    private final TokenService tokenService;
    @Transactional
    public void signUp(SignUpRequest signUpRequest){
        checkExistDuplicateEmail(signUpRequest.getEmail());
        checkExistDuplicateNickname(signUpRequest.getNickname());

        ActiveInfo activeInfo = activeInfoRepository.save(new ActiveInfo());
        Member member = signUpRequest.toMemberWith(activeInfo);

        memberRepository.save(member);
    }

    public AuthenticateResponse login(LoginRequest loginRequest){
        Member member = getMemberByEmail(loginRequest.getEmail());

        checkCorrectPassword(loginRequest.getPassword(), member.getPassword());
        String accessToken = tokenService.generateAccessToken(member.getEmail());

        return AuthenticateResponse.toDto(member, accessToken);
    }

    public void logout(){
        Member member = getMember();
        tokenService.deleteRefreshToken(member.getEmail());
    }

    @Transactional
    public void resign(String password, String activeInfoId, String refreshToken){
        Member member = getMember();
        checkCorrectPassword(password, member.getPassword());
        tokenService.deleteRefreshToken(member.getEmail());
        activeInfoRepository.deleteById(activeInfoId);
        memberRepository.delete(member);
    }

    public AuthenticateResponse refresh(String refreshToken) {
        checkExistRefreshToken(refreshToken);

        String email = tokenService.getEmail(refreshToken);
        Member member = getMemberByEmail(email);

        checkValidRefreshToken(refreshToken, member);

        String accessToken = tokenService.generateAccessToken(email);

        return AuthenticateResponse.toDto(member, accessToken);
    }

    private Member getMember(){
        return (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_MEMBER));
    }

    private void checkExistDuplicateEmail(String email){
        if(memberRepository.existsByEmail(email)){
            throw new RestApiException(DUPLICATE_EMAIL);
        }
    }

    private void checkExistDuplicateNickname(String nickname){
        if(memberRepository.existsByNickname(nickname)){
            throw new RestApiException(DUPLICATE_NICKNAME);
        }
    }

    private void checkCorrectPassword(String inputPassword, String memberPassword){
        if(!PasswordEncryptor.matchPassowrd(inputPassword, memberPassword)){
            throw new RestApiException(NOT_MATCH_PASSWORD);
        }
    }

    private void checkExistRefreshToken(String refreshToken){
        if(refreshToken == null){
            throw new RestApiException(INVALID_REFRESH_TOKEN);
        }
    }

    /**
     * 예외처리
     * (RefreshToken이 만료 됨, 저장된 사용자의 RefreshToken과 일치하지 않음)
     */
    private void checkValidRefreshToken(String refreshToken, Member member){
        if(!tokenService.isTokenValid(refreshToken, member)){
            throw new RestApiException(INVALID_REFRESH_TOKEN);
        }
        tokenService.validateRefreshToken(refreshToken, member.getEmail());
    }
}
