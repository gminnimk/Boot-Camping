package com.sparta.studytrek.domain.review.service;

import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.review.dto.ReviewRequestDto;
import com.sparta.studytrek.domain.review.dto.ReviewResponseDto;
import com.sparta.studytrek.domain.review.entity.Review;
import com.sparta.studytrek.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 작성
     *
     * @param requestDto 리뷰 등록 요청 데이터
     * @param user       요청한 유저의 정보
     * @return
     */
    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto requestDto, User user) {
        Review review = new Review(requestDto, user);
        Review creatReview = reviewRepository.save(review);
        ReviewResponseDto responseDto = new ReviewResponseDto(creatReview);
        return responseDto;
    }
}
