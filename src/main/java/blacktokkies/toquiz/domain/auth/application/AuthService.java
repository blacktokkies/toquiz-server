package blacktokkies.toquiz.domain.auth.application;

import blacktokkies.toquiz.domain.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.domain.activeinfo.domain.ActiveInfo;
import blacktokkies.toquiz.domain.auth.dto.AuthenticateResponse;
import blacktokkies.toquiz.domain.auth.jwt.JwtTokenProvider;
import blacktokkies.toquiz.global.common.annotation.activeInfoId.ActiveInfoIdDto;
import blacktokkies.toquiz.global.common.annotation.auth.MemberEmailDto;
import blacktokkies.toquiz.global.common.error.RestApiException;
import blacktokkies.toquiz.domain.auth.dto.LoginRequest;
import blacktokkies.toquiz.domain.auth.dto.SignUpRequest;
import blacktokkies.toquiz.domain.member.domain.MemberRepository;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.auth.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static blacktokkies.toquiz.domain.auth.exception.AuthErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final ActiveInfoRepository activeInfoRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signUp(SignUpRequest signUpRequest){
        checkExistDuplicateEmail(signUpRequest.getEmail());
        checkExistDuplicateNickname(signUpRequest.getNickname());

        ActiveInfo activeInfo = activeInfoRepository.save(new ActiveInfo());
        Member member = signUpRequest.toMemberWith(activeInfo);

        memberRepository.save(member);

        log.info("회원가입 - 사용자 이메일 : {}", member.getEmail());
    }

    public AuthenticateResponse login(LoginRequest loginRequest){
        Member member = getMemberByEmail(loginRequest.getEmail());

        checkCorrectPassword(loginRequest.getPassword(), member.getPassword());
        String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail());

        log.info("로그인 - 사용자 이메일 : {}", member.getEmail());
        return AuthenticateResponse.of(member, accessToken);
    }

    @Transactional
    public void logout(MemberEmailDto memberEmailDto){
        String email = memberEmailDto.email();
        refreshTokenService.deleteRefreshTokenByEmail(email);

        log.info("로그아웃 - 사용자 이메일 : {}", email);
    }

    @Transactional
    public void resign(MemberEmailDto memberEmailDto, String password, ActiveInfoIdDto activeInfoIdDto){
        String email = memberEmailDto.email();;
        Member member = getMemberByEmail(email);
        checkCorrectPassword(password, member.getPassword()); // 비밀번호가 일치하는지 검증

        // RefreshToken, ActiveInfoId, Member를 DB에서 제거
        refreshTokenService.deleteRefreshTokenByEmail(email);
        activeInfoRepository.deleteById(activeInfoIdDto.activeInfoId());
        memberRepository.delete(member);

        log.info("회원탈퇴 - 사용자 이메일 : {}", email);
    }

    public AuthenticateResponse refresh(String refreshToken) {
        String email = jwtTokenProvider.extractEmailFromRefreshToken(refreshToken);
        String accessToken = jwtTokenProvider.generateAccessToken(email);

        return AuthenticateResponse.of(getMemberByEmail(email), accessToken);
    }

    // ------------ [엔티티 가져오는 메서드] ------------ //
    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_MEMBER));
    }

    // ---------------- [검증 메서드] ---------------- //
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
        if(!PasswordEncryptor.matchPassword(inputPassword, memberPassword)){
            throw new RestApiException(NOT_MATCH_PASSWORD);
        }
    }
}
