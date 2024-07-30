package com.sparta.studytrek.domain.answer.repository;

import com.sparta.studytrek.domain.answer.entity.Answer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerRepositoryCustom{

    Optional<Answer> findByQuestionIdAndId(Long questionId, Long answerId);
}
