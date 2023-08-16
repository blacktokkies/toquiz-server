package blacktokkies.toquiz.domain.panel.dto.response;

import blacktokkies.toquiz.domain.activeinfo.domain.ActivePanel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetMyActiveInfoResponse {
    public List<Long> questionIds;
    public List<Long> likeIds;

    public static GetMyActiveInfoResponse toDto(ActivePanel activePanel){
        return new GetMyActiveInfoResponse(
            activePanel.getCreatedQuestionIds(),
            activePanel.getLikedQuestionIds()
        );
    }
}
