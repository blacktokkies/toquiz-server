package blacktokkies.toquiz.auth;

import blacktokkies.toquiz.auth.dto.response.AuthenticateResponseDto;
import blacktokkies.toquiz.common.success.SuccessMessage;
import blacktokkies.toquiz.common.success.SuccessResponse;
import blacktokkies.toquiz.auth.dto.request.LoginRequestDto;
import blacktokkies.toquiz.auth.dto.request.SignUpRequestDto;
import blacktokkies.toquiz.helper.CookieService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieService cookieService;
    @PostMapping("/api/auth/signup")
    ResponseEntity<SuccessMessage> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);

        return ResponseEntity.ok(SuccessMessage.SIGN_UP);
    }

    @PostMapping("/api/auth/login")
    ResponseEntity<SuccessResponse<AuthenticateResponseDto>> login(@RequestBody @Valid LoginRequestDto loginRequestDto,
                                                                   HttpServletResponse response) {
        AuthenticateResponseDto loginResponse = authService.login(loginRequestDto);

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
    ResponseEntity<SuccessResponse<AuthenticateResponseDto>> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken, HttpServletResponse response){
        AuthenticateResponseDto refreshResponse = authService.refresh(refreshToken);

        response.addCookie(cookieService.issueRefreshTokenCookie(refreshToken));

        return ResponseEntity.ok(new SuccessResponse<>(refreshResponse, HttpStatus.OK.value()));
    }
}
