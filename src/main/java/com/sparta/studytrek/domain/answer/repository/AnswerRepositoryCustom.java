package com.sparta.studytrek.domain.answer.repository;

import com.sparta.studytrek.domain.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnswerRepositoryCustom {

    Page<Answer> findByAllByOrderByCreatedAtDesc(Pageable pageable);
}
