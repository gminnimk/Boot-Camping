package com.sparta.studytrek.domain.reply.repository;

import com.sparta.studytrek.domain.reply.entity.ReviewReply;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {
    List<ReviewReply> findByReviewCommentId(Long commentId);
}
