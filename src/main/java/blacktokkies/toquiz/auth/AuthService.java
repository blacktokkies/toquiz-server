package blacktokkies.toquiz.auth;

import blacktokkies.toquiz.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.activeinfo.domain.ActiveInfo;
import blacktokkies.toquiz.common.error.exception.RestApiException;
import blacktokkies.toquiz.helper.token.JwtService;
import blacktokkies.toquiz.helper.PasswordEncryptor;
import blacktokkies.toquiz.auth.dto.request.LoginRequestDto;
import blacktokkies.toquiz.auth.dto.response.LoginResponseDto;
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

    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
            .orElseThrow(() -> new RestApiException(NOT_EXIST_MEMBER));

        checkCorrectPassword(loginRequestDto.getPassword(), member.getPassword());
        String accessToken = jwtService.generateAccessToken(member.getEmail());

        return LoginResponseDto.toDto(member, accessToken);
    }

    public void logout(){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        refreshTokenService.delete(member.getEmail());
    }

    public String refresh(String refreshToken) {
        System.out.println(refreshToken);
        checkValidRefreshToken(refreshToken);

        String email = jwtService.getSubject(refreshToken);
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_MEMBER));

        return jwtService.refreshAccessToken(refreshToken, member);
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

    private void checkValidRefreshToken(String refreshToken){
        if(refreshToken == null){
            throw new RestApiException(INVALID_REFRESH_TOKEN);
        }
    }
}
