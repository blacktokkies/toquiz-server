package blacktokkies.toquiz.domain.panel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMyPanelsResponse {
    private List<PanelResponse> panels;
    private int nextPage;

    public static GetMyPanelsResponse dto(List<PanelResponse> panels, Pageable pageable){
        return GetMyPanelsResponse.builder()
            .panels(panels)
            .nextPage(pageable.getPageNumber() + 1)
            .build();
    }
}
