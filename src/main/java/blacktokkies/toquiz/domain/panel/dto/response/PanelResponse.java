package blacktokkies.toquiz.domain.panel.dto.response;

import blacktokkies.toquiz.domain.panel.domain.Panel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PanelResponse {
    private Long panelId;
    private String title;
    private String description;
    private long scarpNum;
    private boolean isArchived;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PanelResponse toDto(Panel panel){
        return PanelResponse.builder()
            .panelId(panel.getId())
            .title(panel.getTitle())
            .description(panel.getDescription())
            .isArchived(panel.isArchived())
            .scarpNum(panel.getScarpNum())
            .createdAt(panel.getCreatedDate())
            .updatedAt(panel.getCreatedDate())
            .build();
    }
}
