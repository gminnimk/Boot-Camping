package com.sparta.studytrek.domain.comment.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.comment.dto.CommentRequestDto;
import com.sparta.studytrek.domain.comment.dto.CommentResponseDto;
import com.sparta.studytrek.domain.comment.entity.ReviewComment;
import com.sparta.studytrek.domain.comment.service.ReviewCommentService;
import com.sparta.studytrek.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    /**
     * 리뷰의 댓글 수정
     *
     * @param reviewId    리뷰 ID
     * @param commentId   댓글 ID
     * @param requestDto  요청 받은 댓글 내용
     * @param userDetails 인증된 유저 정보
     * @return 리뷰의 댓글 수정 응답 데이터
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse> updateReviewComment(@PathVariable Long reviewId,
        @PathVariable Long commentId,
        @RequestBody CommentRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto responseDto = reviewCommentService.updateReviewComment(reviewId,
            commentId, requestDto, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("댓글 수정 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 리뷰의 댓글 삭제
     *
     * @param reviewId    리뷰 ID
     * @param commentId   댓글 ID
     * @param userDetails 인증된 유저 정보
     * @return 리뷰의 댓글 삭제 응답 데이터
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> deleteReviewComment(@PathVariable Long reviewId,
        @PathVariable Long commentId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewCommentService.deleteReviewComment(reviewId, commentId, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("댓글 삭제 성공")
            .statuscode(String.valueOf(HttpStatus.NO_CONTENT.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 리뷰의 댓글 전체 조회
     *
     * @param reviewId 리뷰 ID
     * @return 리뷰 댓글 전체 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAllReviewComments(@PathVariable Long reviewId) {
        List<CommentResponseDto> comments = reviewCommentService.getAllReviewComments(reviewId);
        ApiResponse response = ApiResponse.builder()
            .msg("댓글 전체 조회 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(comments)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 리뷰의 댓글 단건 조회
     *
     * @param reviewId  리뷰 ID
     * @param commentId 댓글 ID
     * @return 리뷰 댓글 단건 응답 데이터
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponse> getReviewComment(@PathVariable Long reviewId,
        @PathVariable Long commentId) {
        CommentResponseDto responseDto = reviewCommentService.getReviewComment(reviewId, commentId);
        ApiResponse response = ApiResponse.builder()
            .msg("댓글 단건 조회 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
