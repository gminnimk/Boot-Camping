package com.sparta.studytrek.domain.like.repository;

import com.sparta.studytrek.domain.like.entity.StudyLike;
import com.sparta.studytrek.domain.study.entity.Study;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyLikeRepository extends JpaRepository<StudyLike, Long> {
    int countLikeById(Long studyId);

    Optional<StudyLike> findByIdAndUserId(Long studyId, Long id);
}
