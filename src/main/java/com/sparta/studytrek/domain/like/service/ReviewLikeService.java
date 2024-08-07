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

    /**
     * 좋아요 추가
     *
     * @param reviewId  리뷰 ID
     * @param user  유저 정보
     * @return  좋아요 응답 데이터
     */
    @Transactional
    public int reviewLike(Long reviewId, User user) {
        Review review = reviewRepository.findByReviewId(reviewId);

        Optional<ReviewLike> existingLike = reviewLikeRepository.findByReviewIdAndUserId(reviewId, user.getId());

        if (existingLike.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_LIKE);
        }

        ReviewLike reviewLike = new ReviewLike(review, user);
        reviewLikeRepository.save(reviewLike);
        return reviewLikeRepository.countLikeByReviewId(reviewId);
    }

    /**
     * 좋아요 취소
     *
     * @param reviewId  리뷰 ID
     * @param user  유저 정보
     * @return  좋아요 취소 응답 데이터
     */
    @Transactional
    public int reviewUnlike(Long reviewId, User user) {
        Review review = reviewRepository.findByReviewId(reviewId);

        ReviewLike reviewLike = reviewLikeRepository.findByReviewIdAndUserId(reviewId, user.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_LIKE));

        reviewLikeRepository.delete(reviewLike);
        return reviewLikeRepository.countLikeByReviewId(reviewId);
    }
}
