package com.sparta.studytrek.domain.like.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.like.service.ReviewLikeService;
import com.sparta.studytrek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/camps/{campId}/reviews/{reviewId}/likes")
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PostMapping
    public ResponseEntity<ApiResponse> reviewLike(@PathVariable Long campId,
        @PathVariable Long reviewId,
        @AuthenticationPrincipal UserDetailsImpl userDetails){

        int reviewAllLike = reviewLikeService.reviewLike(reviewId, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("해당 리뷰에 좋아요 성공 : "  + reviewAllLike)
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
