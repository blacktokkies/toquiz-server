package blacktokkies.toquiz.domain.answer.dao;

import blacktokkies.toquiz.domain.answer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
