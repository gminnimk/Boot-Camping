package com.sparta.studytrek.domain.like.repository;

import com.sparta.studytrek.domain.like.entity.RecruitmentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentLikeRepository extends JpaRepository<RecruitmentLike, Long> {
}
