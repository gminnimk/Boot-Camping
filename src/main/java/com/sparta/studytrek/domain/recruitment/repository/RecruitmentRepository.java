package com.sparta.studytrek.domain.recruitment.repository;

import com.sparta.studytrek.domain.recruitment.entity.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    Page<Recruitment> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
