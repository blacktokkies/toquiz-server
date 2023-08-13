package blacktokkies.toquiz.domain.question.dao;

import blacktokkies.toquiz.domain.panel.domain.Panel;
import blacktokkies.toquiz.domain.question.domain.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByPanel(Panel panel, Pageable pageable);
}
