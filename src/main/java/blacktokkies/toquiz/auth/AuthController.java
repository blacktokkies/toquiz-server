package blacktokkies.toquiz.auth;

import blacktokkies.toquiz.common.success.SuccessMessage;
import blacktokkies.toquiz.common.success.SuccessResponse;
import blacktokkies.toquiz.auth.dto.request.LoginRequestDto;
import blacktokkies.toquiz.auth.dto.request.SignUpRequestDto;
import blacktokkies.toquiz.auth.dto.response.LoginResponseDto;
import blacktokkies.toquiz.helper.CookieService;
import blacktokkies.toquiz.helper.token.RefreshToken;
import blacktokkies.toquiz.member.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
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
    ResponseEntity<SuccessResponse<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto loginRequestDto,
                                 HttpServletResponse response) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);

        response.addCookie(cookieService.issueActiveInfoIdCookie(loginResponseDto.getEmail()));
        response.addCookie(cookieService.issueRefreshTokenCookie(loginResponseDto.getEmail()));

        return ResponseEntity.ok(new SuccessResponse<>(loginResponseDto, HttpStatus.OK.value()));
    }

    @PostMapping("/api/auth/logout")
    ResponseEntity<SuccessMessage> logout(HttpServletResponse response){
        authService.logout();

        response.addCookie(cookieService.expireCookie("ActiveInfoId"));
        response.addCookie(cookieService.expireCookie("RefreshToken"));

        return ResponseEntity.ok(SuccessMessage.LOG_OUT);
    }

    @GetMapping("/api/auth/refresh")
    ResponseEntity<SuccessResponse<String>> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken, HttpServletResponse response){
        String accessToken = authService.refresh(refreshToken);

        response.addCookie(cookieService.renewRefreshTokenCookie(refreshToken));

        return ResponseEntity.ok(new SuccessResponse<>(accessToken, HttpStatus.OK.value()));
    }
}
