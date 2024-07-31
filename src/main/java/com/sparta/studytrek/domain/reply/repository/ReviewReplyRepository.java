package com.sparta.studytrek.domain.reply.repository;

import com.sparta.studytrek.domain.reply.entity.ReviewReply;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {
    List<ReviewReply> findByReviewCommentIdOrderByCreatedAtDesc(Long commentId);
    Optional<ReviewReply> findByReviewCommentIdAndId(Long commentId, Long replyId);
}
