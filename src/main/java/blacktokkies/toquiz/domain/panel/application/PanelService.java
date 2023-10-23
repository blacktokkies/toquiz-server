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
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static blacktokkies.toquiz.domain.activeinfo.exception.ActiveInfoException.NOT_EXIST_ACTIVE_INFO;
import static blacktokkies.toquiz.domain.panel.exception.PanelErrorCode.NOT_AUTHORIZED_DELETE;
import static blacktokkies.toquiz.domain.panel.exception.PanelErrorCode.NOT_EXIST_PANEL;
import static blacktokkies.toquiz.global.util.RandomStringGenerator.generateRandomString;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PanelService {
    private final ActiveInfoRepository activeInfoRepository;
    private final PanelRepository panelRepository;

    @Transactional
    public PanelResponse createPanel(Member member, CreatePanelRequest createPanelRequest) {
        Panel panel = panelRepository.save(createPanelRequest.toPanelWith(member, generateSecondaryId()));

        return PanelResponse.toDto(panel);
    }

    public GetMyPanelsResponse getMyPanels(Member member, Pageable pageable){
        Page<Panel> panels = panelRepository.findAllByMember(member, pageable);
        List<PanelResponse> panelResponses =  panels.stream().map(PanelResponse::toDto).toList();
        int nextPage = panels.hasNext() ? pageable.getPageNumber() + 1 : -1;

        return GetMyPanelsResponse.toDto(panelResponses, nextPage);
    }

    @Transactional
    @CacheEvict(cacheNames = "PanelInfo", key = "#panelSid", cacheManager = "rcm")
    public void deletePanel(Member member, String panelSid) {
        checkIsPanelManager(member, panelSid);

        panelRepository.deleteBySid(panelSid);
    }

    @Transactional
    @CachePut(cacheNames = "PanelInfo", key = "#panelSid", cacheManager = "rcm")
    public PanelResponse updatePanel(Member member, UpdatePanelRequest updatePanelRequest, String panelSid) {
        checkIsPanelManager(member, panelSid);

        Panel panel = getPanel(panelSid);
        panel.updatePanelInfo(
            updatePanelRequest.getTitle(),
            updatePanelRequest.getDescription()
        );
        Panel updatedPanel = panelRepository.save(panel);

        return PanelResponse.toDto(updatedPanel);
    }

    public GetMyActiveInfoResponse getMyActiveInfo(String panelSid, String activeInfoId) {
        checkIsExistPanel(panelSid);

        ActiveInfo activeInfo = getActiveInfo(activeInfoId);
        Map<String, ActivePanel> activePanelMap = activeInfo.getActivePanels();

        // 현재 패널이 활동 정보에 존재하지 않으면 새로 추가한다.
        if(!activePanelMap.containsKey(panelSid)){
            activePanelMap.put(panelSid, new ActivePanel());
        }

        return GetMyActiveInfoResponse.toDto(activePanelMap.get(panelSid));
    }

    @Cacheable(cacheNames = "PanelInfo", key = "#panelSid", cacheManager = "rcm")
    public PanelResponse getPanelInfo(String panelSid){
        return PanelResponse.toDto(getPanel(panelSid));
    }

    public String generateSecondaryId(){
        String sid = generateRandomString();
        while(panelRepository.existsBySid(sid)){
            sid = generateRandomString();
        }
        return sid;
    }

    private void checkIsPanelManager(Member member, String panelSid) {
        Panel panel = getPanel(panelSid);

        if(!Objects.equals(member.getId(), panel.getMember().getId())){
            throw new RestApiException(NOT_AUTHORIZED_DELETE);
        }
    }

    private void checkIsExistPanel(String panelSid){
        panelRepository.findBySid(panelSid)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_PANEL));
    }

    public Panel getPanel(String panelSid){
        return panelRepository.findBySid(panelSid)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_PANEL));
    }

    private ActiveInfo getActiveInfo(String activeInfoId) {
        return activeInfoRepository.findById(activeInfoId)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_ACTIVE_INFO));
    }
}
