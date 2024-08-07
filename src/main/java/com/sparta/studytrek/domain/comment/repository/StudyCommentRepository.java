package com.sparta.studytrek.domain.comment.repository;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.comment.entity.StudyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyCommentRepository extends JpaRepository<StudyComment, Long> {

    List<StudyComment> findByStudyId(Long studyId);

    Optional<StudyComment> findByIdAndStudyId(Long commentId, Long studyId);

    default StudyComment findByCommentIdAndStudyId(Long commentId, Long studyId){
        return findByIdAndStudyId(commentId, studyId).orElseThrow(() -> new CustomException(
            ErrorCode.COMMENT_NOT_FOUND));
    };

    default StudyComment findByCommentId(Long commentId){
        return findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
