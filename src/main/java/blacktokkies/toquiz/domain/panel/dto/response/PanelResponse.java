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
    private Long id;
    private String title;
    private String description;
    private Long authorId;
    private long scarpNum;
    private boolean isArchived;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PanelResponse toDto(Panel panel){
        return PanelResponse.builder()
            .id(panel.getId())
            .title(panel.getTitle())
            .description(panel.getDescription())
            .authorId(panel.getMember().getId())
            .isArchived(panel.isArchived())
            .scarpNum(panel.getScarpNum())
            .createdAt(panel.getCreatedDate())
            .updatedAt(panel.getCreatedDate())
            .build();
    }
}
