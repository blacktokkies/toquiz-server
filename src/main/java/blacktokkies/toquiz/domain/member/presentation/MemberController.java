package blacktokkies.toquiz.domain.member.presentation;

import blacktokkies.toquiz.domain.member.application.MemberService;
import blacktokkies.toquiz.global.common.annotation.auth.Auth;
import blacktokkies.toquiz.global.common.annotation.auth.MemberEmailDto;
import blacktokkies.toquiz.domain.member.dto.UpdateMyInfoRequest;
import blacktokkies.toquiz.domain.member.dto.MemberInfoResponse;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("api/members/me")
    public ResponseEntity<SuccessResponse<MemberInfoResponse>> getMyInfo(
        @Auth MemberEmailDto memberEmailDto
    ){
        MemberInfoResponse response = memberService.getMyInfo(memberEmailDto);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @PatchMapping("api/members/me")
    public ResponseEntity<SuccessResponse<MemberInfoResponse>> updateMyInfo(
        @Auth MemberEmailDto memberEmailDto,
        @RequestBody @Valid UpdateMyInfoRequest request
    ){
        MemberInfoResponse response = memberService.updateMyInfo(memberEmailDto, request);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }
}
