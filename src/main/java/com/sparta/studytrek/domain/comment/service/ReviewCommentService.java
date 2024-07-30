package com.sparta.studytrek.domain.comment.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.comment.dto.CommentRequestDto;
import com.sparta.studytrek.domain.comment.dto.CommentResponseDto;
import com.sparta.studytrek.domain.comment.entity.ReviewComment;
import com.sparta.studytrek.domain.comment.repository.ReviewCommentRepository;
import com.sparta.studytrek.domain.review.entity.Review;
import com.sparta.studytrek.domain.review.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {

    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewService reviewService;

    /**
     * 리뷰의 댓글 작성
     *
     * @param reviewId   리뷰 ID
     * @param requestDto 댓글 요청 내용
     * @param user       유저 정보
     * @return 리뷰의 댓글 응답 데이터
     */
    @Transactional
    public CommentResponseDto createReviewComment(Long reviewId, CommentRequestDto requestDto,
        User user) {
        Review review = reviewService.findByReviewId(reviewId);

        ReviewComment reviewComment = new ReviewComment(review, user, requestDto.getContent());
        ReviewComment saveComment = reviewCommentRepository.save(reviewComment);

        return new CommentResponseDto(saveComment);
    }

    /**
     * 리뷰의 댓글 수정
     *
     * @param reviewId   리뷰 ID
     * @param commentId  댓글 ID
     * @param requestDto 댓글 요청 내용
     * @param user       유저 정보
     * @return 리뷰의 댓글 응답 데이터
     */
    @Transactional
    public CommentResponseDto updateReviewComment(Long reviewId, Long commentId,
        CommentRequestDto requestDto, User user) {
        reviewService.findByReviewId(reviewId);

        ReviewComment reviewComment = findByReviewCommentId(commentId);
        reviewComment.updateComment(requestDto.getContent());

        return new CommentResponseDto(reviewComment);
    }

    /**
     * 댓글 찾기
     *
     * @param id 댓글 ID
     * @return 해당 댓글의 정보
     */
    public ReviewComment findByReviewCommentId(Long id) {
        return reviewCommentRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_REVIEW_COMMENT));
    }
}
