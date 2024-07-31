package com.sparta.studytrek.domain.comment.repository;

import com.sparta.studytrek.domain.comment.entity.StudyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyCommentRepository extends JpaRepository<StudyComment, Long> {

    List<StudyComment> findByStudyId(Long studyId);

    Optional<StudyComment> findByIdAndStudyId(Long commentId, Long studyId);
}
