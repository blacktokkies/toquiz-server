package blacktokkies.toquiz.domain.panel.application;

import blacktokkies.toquiz.domain.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.domain.activeinfo.domain.ActiveInfo;
import blacktokkies.toquiz.domain.activeinfo.domain.ActivePanel;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.panel.dao.PanelRepository;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import blacktokkies.toquiz.domain.panel.dto.request.CreatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.request.UpdatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.response.GetMyActiveInfoResponse;
import blacktokkies.toquiz.domain.panel.dto.response.GetMyPanelsResponse;
import blacktokkies.toquiz.domain.panel.dto.response.PanelResponse;
import blacktokkies.toquiz.global.common.error.RestApiException;
import blacktokkies.toquiz.global.util.auth.CookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static blacktokkies.toquiz.domain.activeinfo.exception.ActiveInfoException.NOT_EXIST_ACTIVE_INFO;
import static blacktokkies.toquiz.domain.panel.exception.PanelErrorCode.NOT_AUTHORIZED_DELETE;
import static blacktokkies.toquiz.domain.panel.exception.PanelErrorCode.NOT_EXIST_PANEL;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PanelService {
    private final ActiveInfoRepository activeInfoRepository;
    private final PanelRepository panelRepository;
    private final CookieService cookieService;

    @Transactional
    public PanelResponse createPanel(CreatePanelRequest createPanelRequest) {
        Member member = getMember();
        Panel panel = panelRepository.save(createPanelRequest.toPanelWith(member));

        return PanelResponse.toDto(panel);
    }

    public GetMyPanelsResponse getMyPanels(Pageable pageable){
        Member member = getMember();
        Page<Panel> panels = panelRepository.findAllByMember(member, pageable);
        List<PanelResponse> panelResponses =  panels.stream().map(PanelResponse::toDto).toList();
        int nextPage = panels.hasNext() ? pageable.getPageNumber() + 1 : -1;

        return GetMyPanelsResponse.toDto(panelResponses, nextPage);
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

    public GetMyActiveInfoResponse getMyActiveInfo(Long panelId, String activeInfoId) {
        checkIsExistPanel(panelId);

        ActiveInfo activeInfo = getActiveInfo(activeInfoId);
        Map<Long, ActivePanel> activePanelMap = activeInfo.getActivePanels();

        // 현재 패널이 활동 정보에 존재하지 않으면 새로 추가한다.
        if(!activePanelMap.containsKey(panelId)){
            activePanelMap.put(panelId, new ActivePanel());
        }

        return GetMyActiveInfoResponse.toDto(activePanelMap.get(panelId));
    }

    private ActiveInfo getActiveInfo(String activeInfoId) {
        return activeInfoRepository.findById(activeInfoId)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_ACTIVE_INFO));
    }

    private void checkIsAuthorizedToDelete(Long panelId) {
        Panel panel = getPanel(panelId);
        Member member = getMember();

        if(!Objects.equals(member.getId(), panel.getMember().getId())){
            throw new RestApiException(NOT_AUTHORIZED_DELETE);
        }
    }

    private void checkIsExistPanel(Long panelId){
        panelRepository.findById(panelId)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_PANEL));
    }

    public Panel getPanel(Long panelId){
        return panelRepository.findById(panelId)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_PANEL));
    }

    private Member getMember() {
        return (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
