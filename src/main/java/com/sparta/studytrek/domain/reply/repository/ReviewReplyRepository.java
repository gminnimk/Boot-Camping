package com.sparta.studytrek.domain.reply.repository;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.reply.entity.ReviewReply;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {
    List<ReviewReply> findByReviewCommentIdOrderByCreatedAtDesc(Long commentId);
    Optional<ReviewReply> findByReviewCommentIdAndId(Long commentId, Long replyId);

    default ReviewReply findByReviewReplyId(Long replyId){
        return findById(replyId).orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_REVIEW_REPLY));
    }

    default ReviewReply findByReviewCommentIdAndReplyId(Long commentId, Long replyId){
        return findByReviewCommentIdAndId(commentId, replyId).orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_REVIEW_REPLY));
    }
}
