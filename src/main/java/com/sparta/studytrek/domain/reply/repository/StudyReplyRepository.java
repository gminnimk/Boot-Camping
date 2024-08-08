package com.sparta.studytrek.domain.reply.repository;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.reply.entity.StudyReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyReplyRepository extends JpaRepository<StudyReply, Long> {

    List<StudyReply> findByStudyIdAndStudyCommentId(Long studyId, Long commentId);

    Optional<StudyReply> findByIdAndStudyCommentId(Long replyId, Long commentId);

    default StudyReply findByReplyIdAndStudyCommentId(Long replyId, Long commentId){
        return findByIdAndStudyCommentId(replyId, commentId).orElseThrow(
            () -> new CustomException(ErrorCode.REPLY_NOT_FOUND));
    }
}
