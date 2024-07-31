package com.sparta.studytrek.domain.like.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.like.entity.ReviewLike;
import com.sparta.studytrek.domain.like.repository.ReviewLikeRepository;
import com.sparta.studytrek.domain.review.entity.Review;
import com.sparta.studytrek.domain.review.repository.ReviewRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public int reviewLike(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_REVIEW));

        Optional<ReviewLike> existingLike = reviewLikeRepository.findByReviewIdAndUserId(reviewId, user.getId());

        if (existingLike.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_LIKE);
        }

        ReviewLike reviewLike = new ReviewLike(review, user);
        reviewLikeRepository.save(reviewLike);
        return reviewLikeRepository.countLikeByReviewId(reviewId);
    }

    @Transactional
    public int reviewUnlike(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_REVIEW));

        ReviewLike reviewLike = reviewLikeRepository.findByReviewIdAndUserId(reviewId, user.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_LIKE));

        reviewLikeRepository.delete(reviewLike);
        return reviewLikeRepository.countLikeByReviewId(reviewId);
    }
}
