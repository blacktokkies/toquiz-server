package blacktokkies.toquiz.domain.panel.dto;

import blacktokkies.toquiz.domain.activeinfo.domain.ActivePanel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class GetMyActiveInfoResponse {
    public Set<Long> createdIds;
    public Set<Long> likedIds;

    public static GetMyActiveInfoResponse from(ActivePanel activePanel){
        return new GetMyActiveInfoResponse(
            activePanel.getCreatedQuestionIds(),
            activePanel.getLikedQuestionIds()
        );
    }
}
