package blacktokkies.toquiz.domain.panel.api;

import blacktokkies.toquiz.domain.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.panel.application.PanelService;
import blacktokkies.toquiz.domain.panel.dto.request.CreatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.request.UpdatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.response.GetMyActiveInfoResponse;
import blacktokkies.toquiz.domain.panel.dto.response.GetMyPanelsResponse;
import blacktokkies.toquiz.domain.panel.dto.response.PanelResponse;
import blacktokkies.toquiz.global.common.response.SuccessMessage;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import blacktokkies.toquiz.global.util.auth.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PanelApi {
    private final PanelService panelService;
    private final CookieService cookieService;
    private final ActiveInfoRepository activeInfoRepository;

    @PostMapping("api/panel")
    public ResponseEntity<SuccessResponse<PanelResponse>> createPanel(
        @AuthenticationPrincipal Member member,
        @RequestBody  @Valid CreatePanelRequest createPanelRequest
    ){
        PanelResponse response = panelService.createPanel(member, createPanelRequest);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("api/panels")
    public ResponseEntity<SuccessResponse<GetMyPanelsResponse>> getMyPanels(
        @AuthenticationPrincipal Member member,
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
    ){
        GetMyPanelsResponse response = panelService.getMyPanels(member, pageable);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @DeleteMapping("api/panels/{panelSid}")
    public ResponseEntity<SuccessMessage> deletePanel(
        @AuthenticationPrincipal Member member,
        @PathVariable String panelSid
    ){
        panelService.deletePanel(member, panelSid);

        return ResponseEntity.ok(SuccessMessage.PANEL_DELETE);
    }

    @PatchMapping("api/panels/{panelSid}")
    public ResponseEntity<SuccessResponse<PanelResponse>> updatePanel(
        @RequestBody @Valid UpdatePanelRequest request,
        @PathVariable String panelSid
    ){
        PanelResponse response = panelService.updatePanel(request, panelSid);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("api/panels/{panelSid}")
    public ResponseEntity<SuccessResponse<PanelResponse>> getPanelInfo(@PathVariable String panelSid){
        PanelResponse response = PanelResponse.toDto(panelService.getPanel(panelSid));

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("api/panels/{panelSid}/active-info")
    public ResponseEntity<SuccessResponse<GetMyActiveInfoResponse>> getActiveInfo(
        HttpServletResponse httpResponse,
        @PathVariable String panelSid,
        @CookieValue(value = "active_info_id", required = false) String activeInfoId
    ){
        // ActiveInfoId가 유효하지 않으면, ActiveInfoId를 쿠키로 새로 발급한다.
        if(activeInfoId == null || !activeInfoRepository.existsById(activeInfoId)){
            Cookie activeInfoIdCookie = cookieService.issueActiveInfoIdCookie();
            httpResponse.addCookie(activeInfoIdCookie);

            activeInfoId = activeInfoIdCookie.getValue();
        }

        GetMyActiveInfoResponse response = panelService.getMyActiveInfo(panelSid, activeInfoId);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }
}
