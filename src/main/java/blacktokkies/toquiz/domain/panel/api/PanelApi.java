package blacktokkies.toquiz.domain.panel.api;

import blacktokkies.toquiz.domain.panel.application.PanelService;
import blacktokkies.toquiz.domain.panel.dto.request.CreatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.response.GetMyPanelsResponse;
import blacktokkies.toquiz.domain.panel.dto.response.PanelResponse;
import blacktokkies.toquiz.global.common.response.SuccessMessage;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PanelApi {
    private final PanelService panelService;
    @PostMapping("api/panel")
    public ResponseEntity<SuccessResponse<PanelResponse>> createPanel(
        @RequestBody  @Valid CreatePanelRequest createPanelRequest
    ){
        PanelResponse response = panelService.createPanel(createPanelRequest);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("api/panels")
    public ResponseEntity<SuccessResponse<GetMyPanelsResponse>> getMyPanels(
        @PageableDefault(size = 10, sort = "updatedDate") Pageable pageable
    ){
        List<PanelResponse> panels = panelService.getMyPanels(pageable);
        GetMyPanelsResponse response = GetMyPanelsResponse.dto(panels, pageable);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @DeleteMapping("api/panels/{panelId}")
    public ResponseEntity<SuccessMessage> deletePanel(@PathVariable("panelId") Long panelId){
        panelService.deletePanel(panelId);

        return ResponseEntity.ok(SuccessMessage.PANEL_DELETE);
    }
}
