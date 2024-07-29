package com.sparta.studytrek.domain.review.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.review.dto.ReviewRequestDto;
import com.sparta.studytrek.domain.review.dto.ReviewResponseDto;
import com.sparta.studytrek.domain.review.service.ReviewService;
import com.sparta.studytrek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 작성 API
     *
     * @param requestDto  리뷰 작성 데이터
     * @param userDetails 인증된 유저 정보
     * @return 리뷰 작성 응답 데이터
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createReview(@RequestBody ReviewRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponseDto responseDto = reviewService.createReview(requestDto,
            userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("리뷰 작성 성공")
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 리뷰 수정

    // 리뷰 삭제

    // 리뷰 전체 조회

    // 리뷰 단건 조회

}
