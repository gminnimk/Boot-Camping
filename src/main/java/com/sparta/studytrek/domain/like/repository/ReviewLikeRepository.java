package com.sparta.studytrek.domain.like.repository;

import com.sparta.studytrek.domain.like.entity.ReviewLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByReviewIdAndUserId(Long reviewId, Long id);

    int countLikeByReviewId(Long reviewId);
}
