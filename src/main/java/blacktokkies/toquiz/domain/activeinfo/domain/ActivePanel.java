package blacktokkies.toquiz.domain.activeinfo.domain;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ActivePanel {
    private Set<Long> likedQuestionIds = new HashSet<>();
    private Set<Long> createdQuestionIds = new HashSet<>();
}
