package blacktokkies.toquiz.domain.member.api;

import blacktokkies.toquiz.domain.member.application.MemberService;
import blacktokkies.toquiz.domain.member.dto.response.GetMyInfoResponse;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApi {
    private final MemberService memberService;

    @GetMapping("api/members/me")
    public ResponseEntity<SuccessResponse<GetMyInfoResponse>> getMyInfo(){
        GetMyInfoResponse response = memberService.getMyInfo();
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }
}
