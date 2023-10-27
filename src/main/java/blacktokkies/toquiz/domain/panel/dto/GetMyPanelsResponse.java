package blacktokkies.toquiz.domain.panel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMyPanelsResponse {
    private List<PanelResponse> panels;
    private int nextPage;

    public static GetMyPanelsResponse of(List<PanelResponse> panels, int nextPage){
        return GetMyPanelsResponse.builder()
            .panels(panels)
            .nextPage(nextPage)
            .build();
    }
}
