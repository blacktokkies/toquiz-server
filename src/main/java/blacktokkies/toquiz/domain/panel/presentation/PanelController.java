package blacktokkies.toquiz.domain.panel.presentation;

import blacktokkies.toquiz.global.common.annotation.activeInfoId.ActiveInfoId;
import blacktokkies.toquiz.global.common.annotation.activeInfoId.ActiveInfoIdDto;
import blacktokkies.toquiz.global.common.annotation.auth.Auth;
import blacktokkies.toquiz.global.common.annotation.auth.MemberEmailDto;
import blacktokkies.toquiz.domain.panel.application.PanelService;
import blacktokkies.toquiz.domain.panel.dto.CreatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.UpdatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.GetMyActiveInfoResponse;
import blacktokkies.toquiz.domain.panel.dto.GetMyPanelsResponse;
import blacktokkies.toquiz.domain.panel.dto.PanelResponse;
import blacktokkies.toquiz.global.common.response.SuccessMessage;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PanelController {
    private final PanelService panelService;

    @PostMapping("api/panel")
    public ResponseEntity<SuccessResponse<PanelResponse>> createPanel(
        @Auth MemberEmailDto memberEmailDto,
        @RequestBody  @Valid CreatePanelRequest createPanelRequest
    ){
        PanelResponse response = panelService.createPanel(memberEmailDto, createPanelRequest);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("api/panels")
    public ResponseEntity<SuccessResponse<GetMyPanelsResponse>> getMyPanels(
        @Auth MemberEmailDto memberEmailDto,
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
    ){
        GetMyPanelsResponse response = panelService.getMyPanels(memberEmailDto, pageable);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @DeleteMapping("api/panels/{panelSid}")
    public ResponseEntity<SuccessMessage> deletePanel(
        @Auth MemberEmailDto memberEmailDto,
        @PathVariable String panelSid
    ){
        panelService.deletePanel(memberEmailDto, panelSid);

        return ResponseEntity.ok(SuccessMessage.PANEL_DELETE);
    }

    @PatchMapping("api/panels/{panelSid}")
    public ResponseEntity<SuccessResponse<PanelResponse>> updatePanel(
        @Auth MemberEmailDto memberEmailDto,
        @RequestBody @Valid UpdatePanelRequest request,
        @PathVariable String panelSid
    ){
        PanelResponse response = panelService.updatePanel(memberEmailDto, request, panelSid);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("api/panels/{panelSid}")
    public ResponseEntity<SuccessResponse<PanelResponse>> getPanelInfo(
        @PathVariable String panelSid
    ){
        PanelResponse response = panelService.getPanelInfo(panelSid);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("api/panels/{panelSid}/active-info")
    public ResponseEntity<SuccessResponse<GetMyActiveInfoResponse>> getActiveInfo(
        @PathVariable String panelSid,
        @ActiveInfoId ActiveInfoIdDto activeInfoIdDto
    ){
        GetMyActiveInfoResponse response = panelService.getMyActiveInfo(panelSid, activeInfoIdDto);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }
}
