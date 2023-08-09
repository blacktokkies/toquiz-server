package blacktokkies.toquiz.auth;

import blacktokkies.toquiz.common.error.exception.RestApiException;
import blacktokkies.toquiz.helper.token.JwtService;
import blacktokkies.toquiz.helper.PasswordEncryptor;
import blacktokkies.toquiz.helper.token.RefreshTokenRepository;
import blacktokkies.toquiz.helper.token.RefreshToken;
import blacktokkies.toquiz.auth.dto.request.LoginRequestDto;
import blacktokkies.toquiz.auth.dto.response.LoginResponseDto;
import blacktokkies.toquiz.common.error.errorcode.AuthErrorCode;
import blacktokkies.toquiz.member.MemberRepository;
import blacktokkies.toquiz.member.domain.Member;
import blacktokkies.toquiz.auth.dto.request.SignUpRequestDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private Integer REFRESH_TOKEN_EXPIRATION;

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto){
        checkExistDuplicateEmail(signUpRequestDto.getEmail());
        checkExistDuplicateNickname(signUpRequestDto.getNickname());
        Member member = signUpRequestDto.toMember();

        memberRepository.save(member);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
            .orElseThrow(() -> new RestApiException(AuthErrorCode.NOT_EXIST_MEMBER));

        checkCorrectPassword(loginRequestDto.getPassword(), member.getPassword());
        String accessToken = jwtService.generateAccessToken(member.getEmail());

        return LoginResponseDto.toDto(member, accessToken);
    }

    @Transactional
    public void issueRefreshTokenCookie(HttpServletResponse response, String email){
        String refreshToken = jwtService.generateRefreshToken(email);

        refreshTokenRepository.save(new RefreshToken(email, refreshToken));

        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setMaxAge(REFRESH_TOKEN_EXPIRATION);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }



    void checkExistDuplicateEmail(String email){
        if(memberRepository.existsByEmail(email)){
            throw new RestApiException(AuthErrorCode.DUPLICATE_EMAIL);
        }
    }

    void checkExistDuplicateNickname(String nickname){
        if(memberRepository.existsByNickname(nickname)){
            throw new RestApiException(AuthErrorCode.DUPLICATE_NICKNAME);
        }
    }

    void checkCorrectPassword(String inputPassword, String memberPassword){
        if(!PasswordEncryptor.matchPassowrd(inputPassword, memberPassword)){
            throw new RestApiException(AuthErrorCode.INVALID_PASSWORD);
        }
    }
}
