package blacktokkies.toquiz.domain.panel.dao;

import blacktokkies.toquiz.domain.member.domain.Member;
import blacktokkies.toquiz.domain.panel.domain.Panel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PanelRepository extends JpaRepository<Panel, Long> {
    Page<Panel> findAllByMember(Member member, Pageable pageable);

    Optional<Panel> findBySid(String sid);
    boolean existsBySid(String sid);
    void deleteBySid(String sid);
}
