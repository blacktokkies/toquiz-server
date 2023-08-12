package blacktokkies.toquiz.domain.panel.dto.response;

import blacktokkies.toquiz.domain.panel.domain.Panel;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePanelResponse {
    private Long panel_id;
    private Long member_id;
    private String title;
    private String description;
    private final int scrapNum = 0;
    private final boolean isArchived = false;

    public static CreatePanelResponse toDto(Panel panel){
        return CreatePanelResponse.builder()
            .title(panel.getTitle())
            .panel_id(panel.getId())
            .member_id(panel.getMember().getId())
            .description(panel.getDescription())
            .build();
    }
}
