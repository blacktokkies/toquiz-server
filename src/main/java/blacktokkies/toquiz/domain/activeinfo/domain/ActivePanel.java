package blacktokkies.toquiz.domain.activeinfo.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ActivePanel {
    private List<Long> likedQuestionIds = new ArrayList<>();
    private List<Long> createdQuestionIds = new ArrayList<>();
}
