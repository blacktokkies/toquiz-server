package blacktokkies.toquiz.domain.panel.application;

import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.panel.dao.PanelRepository;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import blacktokkies.toquiz.domain.panel.dto.request.CreatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.response.CreatePanelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PanelService {
    private final PanelRepository panelRepository;
    public CreatePanelResponse create(CreatePanelRequest createPanelRequest) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Panel panel = panelRepository.save(createPanelRequest.toPanelWith(member));
        return CreatePanelResponse.toDto(panel);
    }
}
