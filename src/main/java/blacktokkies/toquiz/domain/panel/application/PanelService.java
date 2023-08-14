package blacktokkies.toquiz.domain.panel.application;

import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.panel.dao.PanelRepository;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import blacktokkies.toquiz.domain.panel.dto.request.CreatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.request.UpdatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.response.PanelResponse;
import blacktokkies.toquiz.global.common.error.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static blacktokkies.toquiz.domain.panel.exception.PanelErrorCode.NOT_AUTHORIZED_DELETE;
import static blacktokkies.toquiz.domain.panel.exception.PanelErrorCode.NOT_EXIST_PANEL;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PanelService {
    private final PanelRepository panelRepository;

    @Transactional
    public PanelResponse createPanel(CreatePanelRequest createPanelRequest) {
        Member member = getMember();
        Panel panel = panelRepository.save(createPanelRequest.toPanelWith(member));

        return PanelResponse.toDto(panel);
    }

    public List<PanelResponse> getMyPanels(Pageable pageable){
        Member member = getMember();
        List<Panel> panels = panelRepository.findAllByMember(member, pageable);

        return panels.stream().map(PanelResponse::toDto).toList();
    }

    @Transactional
    public void deletePanel(Long panelId) {
        checkIsAuthorizedToDelete(panelId);

        panelRepository.deleteById(panelId);
    }

    @Transactional
    public PanelResponse updatePanel(UpdatePanelRequest updatePanelRequest, Long panelId) {
        Panel panel = getPanel(panelId);
        panel.updatePanelInfo(
            updatePanelRequest.getTitle(),
            updatePanelRequest.getDescription()
        );
        Panel updatedPanel = panelRepository.save(panel);

        return PanelResponse.toDto(updatedPanel);
    }

    private void checkIsAuthorizedToDelete(Long panelId) {
        Panel panel = getPanel(panelId);
        Member member = getMember();

        if(!Objects.equals(member.getId(), panel.getMember().getId())){
            throw new RestApiException(NOT_AUTHORIZED_DELETE);
        }
    }

    public Panel getPanel(Long panelId){
        return panelRepository.findById(panelId)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_PANEL));
    }

    private Member getMember() {
        return (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
