package com.sparta.studytrek.domain.comment.repository;

import com.sparta.studytrek.domain.comment.entity.StudyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyCommentRepository extends JpaRepository<StudyComment, Long> {
}
