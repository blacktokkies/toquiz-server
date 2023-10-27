package blacktokkies.toquiz.domain.auth.presentation;

import blacktokkies.toquiz.global.common.annotation.activeInfoId.ActiveInfoId;
import blacktokkies.toquiz.global.common.annotation.activeInfoId.ActiveInfoIdDto;
import blacktokkies.toquiz.global.common.annotation.auth.Auth;
import blacktokkies.toquiz.global.common.annotation.auth.MemberEmailDto;
import blacktokkies.toquiz.domain.auth.dto.ResignRequest;
import blacktokkies.toquiz.domain.auth.dto.AuthenticateResponse;
import blacktokkies.toquiz.global.common.response.SuccessMessage;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import blacktokkies.toquiz.domain.auth.dto.LoginRequest;
import blacktokkies.toquiz.domain.auth.dto.SignUpRequest;
import blacktokkies.toquiz.domain.auth.application.AuthService;
import blacktokkies.toquiz.domain.auth.application.CookieService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieService cookieService;

    @PostMapping("/api/auth/signup")
    ResponseEntity<SuccessMessage> signUp(
        @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        authService.signUp(signUpRequest);

        return ResponseEntity.ok(SuccessMessage.SIGN_UP);
    }

    @PostMapping("/api/auth/login")
    ResponseEntity<SuccessResponse<AuthenticateResponse>> login(
        HttpServletResponse httpResponse,
        @RequestBody @Valid LoginRequest loginRequest
    ){
        AuthenticateResponse loginResponse = authService.login(loginRequest);

        httpResponse.addCookie(cookieService.issueActiveInfoIdCookieByEmail(loginResponse.getEmail()));
        httpResponse.addCookie(cookieService.issueRefreshTokenCookieByEmail(loginResponse.getEmail()));

        return ResponseEntity.ok(new SuccessResponse<>(loginResponse));
    }

    @PostMapping("/api/auth/logout")
    ResponseEntity<SuccessMessage> logout(
        HttpServletResponse httpResponse,
        @Auth MemberEmailDto memberEmailDto
    ){
        authService.logout(memberEmailDto);

        httpResponse.addCookie(cookieService.expireCookie("active_info_id"));
        httpResponse.addCookie(cookieService.expireCookie("refresh_token"));

        return ResponseEntity.ok(SuccessMessage.LOGOUT);
    }
    @PostMapping("api/auth/resign")
    public ResponseEntity<SuccessMessage> deleteMyInfo(
        @RequestBody @Valid ResignRequest request,
        HttpServletResponse httpResponse,
        @Auth MemberEmailDto memberEmailDto,
        @ActiveInfoId ActiveInfoIdDto activeInfoIdDto
    ){
        authService.resign(memberEmailDto, request.getPassword(), activeInfoIdDto);

        httpResponse.addCookie(cookieService.expireCookie("active_info_id"));
        httpResponse.addCookie(cookieService.expireCookie("refresh_token"));

        return ResponseEntity.ok(SuccessMessage.RESIGN);
    }

    @PostMapping ("/api/auth/refresh")
    ResponseEntity<SuccessResponse<AuthenticateResponse>> refresh(
        HttpServletResponse httpResponse,
        @CookieValue(name = "refresh_token") String refreshToken
    ){
        AuthenticateResponse refreshResponse = authService.refresh(refreshToken);

        httpResponse.addCookie(cookieService.issueRefreshTokenCookieByEmail(refreshResponse.getEmail()));

        return ResponseEntity.ok(new SuccessResponse<>(refreshResponse));
    }
}