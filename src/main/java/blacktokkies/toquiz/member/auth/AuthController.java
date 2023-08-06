package blacktokkies.toquiz.member.auth;

import blacktokkies.toquiz.member.dto.SignUpRequestDto;
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
    ResponseEntity<Long> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto){
        Long savedMemberId = authService.signUp(signUpRequestDto);

        return new ResponseEntity<>(savedMemberId, HttpStatus.OK);
    }
}
