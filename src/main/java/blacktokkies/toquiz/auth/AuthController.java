package blacktokkies.toquiz.auth;

import blacktokkies.toquiz.common.success.SuccessMessage;
import blacktokkies.toquiz.common.success.SuccessResponse;
import blacktokkies.toquiz.auth.dto.request.LoginRequestDto;
import blacktokkies.toquiz.auth.dto.request.SignUpRequestDto;
import blacktokkies.toquiz.auth.dto.response.LoginResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/api/auth/signup")
    ResponseEntity<SuccessMessage> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);

        return ResponseEntity.ok(SuccessMessage.SIGN_UP);
    }

    @PostMapping("/api/auth/login")
    ResponseEntity<SuccessResponse<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto loginRequestDto,
                                 HttpServletResponse response) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        authService.issueRefreshTokenCookie(response, loginResponseDto.getEmail());

        return ResponseEntity.ok(new SuccessResponse<>(loginResponseDto, HttpStatus.OK.value()));
    }
}
