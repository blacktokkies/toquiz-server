package blacktokkies.toquiz.domain.panel.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PanelRepository extends JpaRepository<Panel, Long> {
    Page<Panel> findAllByMemberEmail(String email, Pageable pageable);

    Optional<Panel> findBySid(String sid);
    boolean existsBySid(String sid);
    void deleteBySid(String sid);
}
