package com.sparta.studytrek.domain.like.repository;

import com.sparta.studytrek.domain.like.entity.ReviewLike;
import com.sparta.studytrek.domain.review.entity.Review;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByIdAndUserId(Long reviewId, Long id);

    int countLikeById(Long reviewId);
}
