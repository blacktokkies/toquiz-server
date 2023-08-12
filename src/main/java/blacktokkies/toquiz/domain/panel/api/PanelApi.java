package blacktokkies.toquiz.domain.panel.api;

import blacktokkies.toquiz.domain.panel.application.PanelService;
import blacktokkies.toquiz.domain.panel.dto.request.CreatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.response.CreatePanelResponse;
import blacktokkies.toquiz.global.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PanelApi {
    private final PanelService panelService;
    @PostMapping("api/panel")
    ResponseEntity<SuccessResponse<CreatePanelResponse>> create(@RequestBody  @Valid CreatePanelRequest createPanelRequest){
        CreatePanelResponse response = panelService.create(createPanelRequest);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }
}
