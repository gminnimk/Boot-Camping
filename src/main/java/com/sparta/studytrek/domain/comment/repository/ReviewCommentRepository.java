package com.sparta.studytrek.domain.comment.repository;

import com.sparta.studytrek.domain.comment.entity.ReviewComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
    List<ReviewComment> findByReviewId(Long reviewId);
    Optional<ReviewComment> findByReviewIdAndId(Long reviewId, Long id);
}
