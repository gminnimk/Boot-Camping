package com.sparta.studytrek.domain.reply.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.reply.dto.ReplyRequestDto;
import com.sparta.studytrek.domain.reply.dto.ReplyResponseDto;
import com.sparta.studytrek.domain.reply.entity.ReviewReply;
import com.sparta.studytrek.domain.reply.service.ReviewReplyService;
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
@RequestMapping("/api/reviews/{reviewId}/comments/{commentId}/reply")
public class ReviewReplyController {

    private final ReviewReplyService reviewReplyService;

    /**
     * 리뷰 댓글의 대댓글 작성
     *
     * @param reviewId 리뷰 ID
     * @param commentId 댓글 ID
     * @param requestDto 요청 받은 대댓글 내용
     * @param userDetails 인증된 유저 정보
     * @return 리뷰 댓글의 대댓글 작성 응답 데이터
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createReviewReply(@PathVariable Long reviewId,
        @PathVariable Long commentId, @RequestBody
    ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReplyResponseDto responseDto = reviewReplyService.createReviewReply(reviewId, commentId, requestDto, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("대댓글 등록 성공")
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
