package blacktokkies.toquiz.domain.panel.application;

import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.panel.dao.PanelRepository;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import blacktokkies.toquiz.domain.panel.dto.request.CreatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.response.PanelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PanelService {
    private final PanelRepository panelRepository;

    @Transactional
    public PanelResponse create(CreatePanelRequest createPanelRequest) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Panel panel = panelRepository.save(createPanelRequest.toPanelWith(member));

        return PanelResponse.toDto(panel);
    }

    public List<PanelResponse> getMyPanels(Pageable pageable){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Panel> panels = panelRepository.findAllByMember(member, pageable);

        return panels.stream().map(PanelResponse::toDto).toList();
    }
}
