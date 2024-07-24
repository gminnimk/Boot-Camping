package com.sparta.studytrek.domain.like.repository;

import com.sparta.studytrek.domain.like.entity.RecruitLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitLikeRepository extends JpaRepository<RecruitLike, Long> {
}
