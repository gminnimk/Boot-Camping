package com.sparta.studytrek.domain.comment.repository;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.comment.entity.ReviewComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
    List<ReviewComment> findByReviewIdOrderByCreatedAtDesc(Long reviewId);
    Optional<ReviewComment> findByReviewIdAndId(Long reviewId, Long id);

    default ReviewComment findByReviewCommentId(Long commentId){
        return findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_REVIEW_COMMENT));
    }
}
