package com.sparta.studytrek.domain.question.repository;

import com.sparta.studytrek.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionRepositoryCustom {

    Page<Question> findByAllByOrderByCreatedAtDesc(Pageable pageable);
}