package blacktokkies.toquiz.domain.panel.dto.response;

import blacktokkies.toquiz.domain.activeinfo.domain.ActivePanel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class GetMyActiveInfoResponse {
    public Set<Long> createdIds;
    public Set<Long> likedIds;

    public static GetMyActiveInfoResponse toDto(ActivePanel activePanel){
        return new GetMyActiveInfoResponse(
            activePanel.getCreatedQuestionIds(),
            activePanel.getLikedQuestionIds()
        );
    }
}
