package com.sparta.studytrek.domain.like.repository;

import com.sparta.studytrek.domain.like.entity.RecruitmentLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentLikeRepository extends JpaRepository<RecruitmentLike, Long> {
    Optional<RecruitmentLike> findByRecruitmentIdAndUserId(Long recruitmentId, Long userId);
    int countLikeByRecruitmentId(Long campId);
}
