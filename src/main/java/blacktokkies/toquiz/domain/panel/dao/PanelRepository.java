package blacktokkies.toquiz.domain.panel.dao;

import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PanelRepository extends JpaRepository<Panel, Long> {
    List<Panel> findAllByMember(Member member, Pageable pageable);
}
