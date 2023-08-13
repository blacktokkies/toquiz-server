package blacktokkies.toquiz.domain.panel.api;

import blacktokkies.toquiz.domain.panel.application.PanelService;
import blacktokkies.toquiz.domain.panel.dto.request.CreatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.response.PanelResponse;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PanelApi {
    private final PanelService panelService;
    @PostMapping("api/panel")
    ResponseEntity<SuccessResponse<PanelResponse>> create(@RequestBody  @Valid CreatePanelRequest createPanelRequest){
        PanelResponse response = panelService.create(createPanelRequest);

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("api/panels")
    public ResponseEntity<SuccessResponse<List<PanelResponse>>> getMyPanels(
        @PageableDefault(size = 10, sort = "updatedDate") Pageable pageable){
        List<PanelResponse> panels = panelService.getMyPanels(pageable);

        return ResponseEntity.ok(new SuccessResponse<>(panels));
    }
}
