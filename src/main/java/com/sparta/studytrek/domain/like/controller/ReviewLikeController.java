package com.sparta.studytrek.domain.like.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.common.ResponseText;
import com.sparta.studytrek.domain.like.service.ReviewLikeService;
import com.sparta.studytrek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews/{reviewId}/likes")
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    /**
     * 리뷰 좋아요 API
     *
     * @param reviewId      리뷰 ID
     * @param userDetails   인증된 유저 정보
     * @return  좋아요 응답 데이터
     */
    @PostMapping
    public ResponseEntity<ApiResponse> reviewLike(@PathVariable Long reviewId,
        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        int reviewAllLike = reviewLikeService.reviewLike(reviewId, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.LIKE_RECRUIT_SUCCESS.format(reviewAllLike))
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 리뷰 좋아요 취소 API
     *
     * @param reviewId      리뷰 ID
     * @param userDetails   인증된 유저 정보
     * @return  좋아요 응답 데이터
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse> reviewUnlike(@PathVariable Long reviewId,
        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        int reviewAllLike = reviewLikeService.reviewUnlike(reviewId, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.LIKE_CALL_OFF_SUCCESS.format(reviewAllLike))
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
