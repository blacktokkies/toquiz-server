package blacktokkies.toquiz.domain.member.api;

import blacktokkies.toquiz.domain.member.application.MemberService;
import blacktokkies.toquiz.domain.member.dto.response.MemberInfoResponse;
import blacktokkies.toquiz.global.common.response.SuccessMessage;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApi {
    private final MemberService memberService;

    @GetMapping("api/members/me")
    public ResponseEntity<SuccessResponse<MemberInfoResponse>> getMyInfo(){
        MemberInfoResponse response = memberService.getMyInfo();
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @DeleteMapping("api/members/me")
    public ResponseEntity<SuccessMessage> deleteMyInfo(){
        memberService.deleteMyInfo();
        return ResponseEntity.ok(SuccessMessage.RESIGN);
    }
}
