package blacktokkies.toquiz.domain.answer.domain;

import blacktokkies.toquiz.domain.answer.domain.Answer;
import blacktokkies.toquiz.domain.question.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionOrderByCreatedDateDesc(Question question);
}
