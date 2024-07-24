package com.sparta.studytrek.domain.like.repository;

import com.sparta.studytrek.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLikeRepository extends JpaRepository<Review, Long> {
}
