package com.sparta.studytrek.domain.study.repository;

import com.sparta.studytrek.domain.study.entity.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudyRepositoryCustom {
    // 페이징 처리된 결과를 얻기 위한 커스텀 메서드를 정의
    Page<Study> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
