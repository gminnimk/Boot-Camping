package com.sparta.studytrek.domain.comment.repository;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.comment.entity.AnswerComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerCommentRepository extends JpaRepository<AnswerComment, Long> {

    List<AnswerComment> findByAnswerIdOrderByCreatedAtDesc(Long answerId);

    Optional<AnswerComment> findByAnswerIdAndId(Long answerId, Long commentId);

    default AnswerComment findByAnswerCommentId(Long commentId){
        return findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_ANSWER_COMMENT));
    }

    default AnswerComment findByAnswerIdAndCommentId(Long answerId, Long commentId){
        return findByAnswerIdAndId(answerId, commentId).orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_ANSWER_COMMENT));
    }
}
