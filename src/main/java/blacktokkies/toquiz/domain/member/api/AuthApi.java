package blacktokkies.toquiz.domain.member.api;

import blacktokkies.toquiz.domain.member.dto.response.AuthenticateResponse;
import blacktokkies.toquiz.global.common.response.SuccessMessage;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import blacktokkies.toquiz.domain.member.dto.request.LoginRequest;
import blacktokkies.toquiz.domain.member.dto.request.SignUpRequest;
import blacktokkies.toquiz.domain.member.application.AuthService;
import blacktokkies.toquiz.global.util.auth.CookieService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthApi {
    private final AuthService authService;
    private final CookieService cookieService;
    @PostMapping("/api/auth/signup")
    ResponseEntity<SuccessMessage> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);

        return ResponseEntity.ok(SuccessMessage.SIGN_UP);
    }

    @PostMapping("/api/auth/login")
    ResponseEntity<SuccessResponse<AuthenticateResponse>> login(@RequestBody @Valid LoginRequest loginRequest,
                                                                   HttpServletResponse response) {
        AuthenticateResponse loginResponse = authService.login(loginRequest);

        response.addCookie(cookieService.issueActiveInfoIdCookie(loginResponse.getEmail()));
        response.addCookie(cookieService.issueRefreshTokenCookie(loginResponse.getEmail()));

        return ResponseEntity.ok(new SuccessResponse<>(loginResponse, HttpStatus.OK.value()));
    }

    @PostMapping("/api/auth/logout")
    ResponseEntity<SuccessMessage> logout(HttpServletResponse response){
        authService.logout();

        response.addCookie(cookieService.expireCookie("ActiveInfoId"));
        response.addCookie(cookieService.expireCookie("RefreshToken"));

        return ResponseEntity.ok(SuccessMessage.LOG_OUT);
    }

    @PostMapping ("/api/auth/refresh")
    ResponseEntity<SuccessResponse<AuthenticateResponse>> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken, HttpServletResponse response){
        AuthenticateResponse refreshResponse = authService.refresh(refreshToken);

        response.addCookie(cookieService.issueRefreshTokenCookie(refreshResponse.getEmail()));

        return ResponseEntity.ok(new SuccessResponse<>(refreshResponse, HttpStatus.OK.value()));
    }
}
