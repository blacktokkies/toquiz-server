package blacktokkies.toquiz.domain.panel.application;

import blacktokkies.toquiz.domain.activeinfo.ActiveInfoRepository;
import blacktokkies.toquiz.domain.activeinfo.domain.ActiveInfo;
import blacktokkies.toquiz.domain.activeinfo.domain.ActivePanel;
import blacktokkies.toquiz.global.common.annotation.activeInfoId.ActiveInfoIdDto;
import blacktokkies.toquiz.global.common.annotation.auth.MemberEmailDto;
import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.member.domain.MemberRepository;
import blacktokkies.toquiz.domain.panel.domain.PanelRepository;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import blacktokkies.toquiz.domain.panel.dto.CreatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.UpdatePanelRequest;
import blacktokkies.toquiz.domain.panel.dto.GetMyActiveInfoResponse;
import blacktokkies.toquiz.domain.panel.dto.GetMyPanelsResponse;
import blacktokkies.toquiz.domain.panel.dto.PanelResponse;
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
import static blacktokkies.toquiz.domain.auth.exception.AuthErrorCode.NOT_EXIST_MEMBER;
import static blacktokkies.toquiz.domain.panel.exception.PanelErrorCode.NOT_AUTHORIZED_DELETE;
import static blacktokkies.toquiz.domain.panel.exception.PanelErrorCode.NOT_EXIST_PANEL;
import static blacktokkies.toquiz.domain.panel.util.RandomStringGenerator.generateRandomString;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PanelService {
    private final ActiveInfoRepository activeInfoRepository;
    private final PanelRepository panelRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PanelResponse createPanel(MemberEmailDto memberEmailDto, CreatePanelRequest createPanelRequest) {
        Member member = getMemberByEmail(memberEmailDto.email());
        Panel panel = panelRepository.save(new Panel(member, createPanelRequest, generateSecondaryId()));

        return PanelResponse.from(panel);
    }

    public GetMyPanelsResponse getMyPanels(MemberEmailDto memberEmailDto, Pageable pageable){
        Page<Panel> panels = panelRepository.findAllByMemberEmail(memberEmailDto.email(), pageable);
        List<PanelResponse> panelResponses =  panels.stream().map(PanelResponse::from).toList();
        int nextPage = panels.hasNext() ? pageable.getPageNumber() + 1 : -1;

        return GetMyPanelsResponse.of(panelResponses, nextPage);
    }

    @Transactional
    @CacheEvict(cacheNames = "PanelInfo", key = "#panelSid", cacheManager = "rcm")
    public void deletePanel(MemberEmailDto memberEmailDto, String panelSid) {
        Member member = getMemberByEmail(memberEmailDto.email());
        checkIsPanelManager(member, getPanel(panelSid));

        panelRepository.deleteBySid(panelSid);
    }

    @Transactional
    @CachePut(cacheNames = "PanelInfo", key = "#panelSid", cacheManager = "rcm")
    public PanelResponse updatePanel(MemberEmailDto memberEmailDto,
                                     UpdatePanelRequest updatePanelRequest,
                                     String panelSid
    ) {
        Member member = getMemberByEmail(memberEmailDto.email());
        Panel panel = getPanel(panelSid);
        checkIsPanelManager(member, panel);

        panel.updatePanelInfo(
            updatePanelRequest.getTitle(),
            updatePanelRequest.getDescription()
        );
        Panel updatedPanel = panelRepository.save(panel);

        return PanelResponse.from(updatedPanel);
    }

    public GetMyActiveInfoResponse getMyActiveInfo(String panelSid, ActiveInfoIdDto activeInfoIdDto) {
        checkIsExistPanel(panelSid);

        ActiveInfo activeInfo = getActiveInfo(activeInfoIdDto.activeInfoId());
        Map<String, ActivePanel> activePanelMap = activeInfo.getActivePanels();

        // 현재 패널이 활동 정보에 존재하지 않으면 새로 추가한다.
        if(!activePanelMap.containsKey(panelSid)){
            activePanelMap.put(panelSid, new ActivePanel());
        }

        return GetMyActiveInfoResponse.from(activePanelMap.get(panelSid));
    }

    @Cacheable(cacheNames = "PanelInfo", key = "#panelSid", cacheManager = "rcm")
    public PanelResponse getPanelInfo(String panelSid){
        return PanelResponse.from(getPanel(panelSid));
    }

    public String generateSecondaryId(){
        String sid = generateRandomString();
        while(panelRepository.existsBySid(sid)){
            sid = generateRandomString();
        }
        return sid;
    }

    // ------------ [엔티티 가져오는 메서드] ------------ //
    public Member getMemberByEmail(String email){
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_MEMBER));
    }

    public Panel getPanel(String panelSid){
        return panelRepository.findBySid(panelSid)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_PANEL));
    }

    private ActiveInfo getActiveInfo(String activeInfoId) {
        return activeInfoRepository.findById(activeInfoId)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_ACTIVE_INFO));
    }

    // ---------------- [검증 메서드] ---------------- //
    private void checkIsPanelManager(Member member, Panel panel) {
        if(!Objects.equals(member.getId(), panel.getMember().getId())){
            throw new RestApiException(NOT_AUTHORIZED_DELETE);
        }
    }

    private void checkIsExistPanel(String panelSid){
        panelRepository.findBySid(panelSid)
            .orElseThrow(() -> new RestApiException(NOT_EXIST_PANEL));
    }
}
