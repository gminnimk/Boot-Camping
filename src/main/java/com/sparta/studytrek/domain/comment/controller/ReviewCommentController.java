package com.sparta.studytrek.domain.comment.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.comment.dto.CommentRequestDto;
import com.sparta.studytrek.domain.comment.dto.CommentResponseDto;
import com.sparta.studytrek.domain.comment.service.ReviewCommentService;
import com.sparta.studytrek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews/{reviewId}/comments")
public class ReviewCommentController {

    private final ReviewCommentService reviewCommentService;

    /**
     * 리뷰의 댓글 작성
     *
     * @param reviewId    리뷰 ID
     * @param requestDto  요청 받은 댓글 내용
     * @param userDetails 인증된 유저 정보
     * @return 리뷰의 댓글 작성 응답 데이터
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createReviewComment(@PathVariable Long reviewId,
        @RequestBody CommentRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto responseDto = reviewCommentService.createReviewComment(reviewId,
            requestDto, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("댓글 등록 성공")
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
