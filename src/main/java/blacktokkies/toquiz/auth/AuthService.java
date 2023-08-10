package blacktokkies.toquiz.auth;

import blacktokkies.toquiz.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.activeinfo.domain.ActiveInfo;
import blacktokkies.toquiz.auth.dto.response.AuthenticateResponseDto;
import blacktokkies.toquiz.common.error.exception.RestApiException;
import blacktokkies.toquiz.helper.token.JwtService;
import blacktokkies.toquiz.helper.PasswordEncryptor;
import blacktokkies.toquiz.auth.dto.request.LoginRequestDto;
import blacktokkies.toquiz.helper.token.RefreshTokenService;
import blacktokkies.toquiz.member.MemberRepository;
import blacktokkies.toquiz.member.domain.Member;
import blacktokkies.toquiz.auth.dto.request.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static blacktokkies.toquiz.common.error.errorcode.AuthErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final ActiveInfoRepository activeInfoRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto){
        checkExistDuplicateEmail(signUpRequestDto.getEmail());
        checkExistDuplicateNickname(signUpRequestDto.getNickname());

        ActiveInfo activeInfo = activeInfoRepository.save(new ActiveInfo());
        Member member = signUpRequestDto.toMemberWith(activeInfo);

        memberRepository.save(member);
    }

    public AuthenticateResponseDto login(LoginRequestDto loginRequestDto){
        Member member = getMemberByEmail(loginRequestDto.getEmail());

        checkCorrectPassword(loginRequestDto.getPassword(), member.getPassword());
        String accessToken = jwtService.generateAccessToken(member.getEmail());

        return AuthenticateResponseDto.toDto(member, accessToken);
    }

    public void logout(){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        refreshTokenService.delete(member.getEmail());
    }

    public AuthenticateResponseDto refresh(String refreshToken) {
        checkExistRefreshToken(refreshToken);

        String email = jwtService.getSubject(refreshToken);
        Member member = getMemberByEmail(email);

        checkValidRefreshToken(refreshToken, member);

        String accessToken = jwtService.generateAccessToken(email);

        return AuthenticateResponseDto.toDto(member, accessToken);
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
            throw new RestApiException(INVALID_PASSWORD);
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
        if(!jwtService.isTokenValid(refreshToken, member)){
            throw new RestApiException(INVALID_REFRESH_TOKEN);
        }
        refreshTokenService.validate(refreshToken, member.getEmail());
    }
}
