package blacktokkies.toquiz.domain.member.api;

import blacktokkies.toquiz.domain.member.application.MemberService;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.member.dto.request.UpdateMyInfoRequest;
import blacktokkies.toquiz.domain.member.dto.response.MemberInfoResponse;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberApi {
    private final MemberService memberService;

    @GetMapping("api/members/me")
    public ResponseEntity<SuccessResponse<MemberInfoResponse>> getMyInfo(
        @AuthenticationPrincipal Member member
    ){
        MemberInfoResponse response = memberService.getMyInfo(member);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @PatchMapping("api/members/me")
    public ResponseEntity<SuccessResponse<MemberInfoResponse>> updateMyInfo(
        @AuthenticationPrincipal Member member,
        @RequestBody @Valid UpdateMyInfoRequest request
        ){
        MemberInfoResponse response = memberService.updateMyInfo(member, request);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }
}
