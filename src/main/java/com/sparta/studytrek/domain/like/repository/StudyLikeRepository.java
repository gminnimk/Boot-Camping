package com.sparta.studytrek.domain.like.repository;

import com.sparta.studytrek.domain.like.entity.StudyLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyLikeRepository extends JpaRepository<StudyLike, Long> {
    int countLikeByStudyId(Long studyId);

    Optional<StudyLike> findByStudyIdAndUserId(Long studyId, Long id);
}
