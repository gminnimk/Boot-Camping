package com.sparta.studytrek.domain.like.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.like.entity.StudyLike;
import com.sparta.studytrek.domain.like.repository.StudyLikeRepository;
import com.sparta.studytrek.domain.study.entity.Study;
import com.sparta.studytrek.domain.study.repository.StudyRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyLikeService {

    private final StudyLikeRepository studyLikeRepository;
    private final StudyRepository studyRepository;

    @Transactional
    public int studyLike(Long studyId, User user) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        Optional<StudyLike> existingLike = studyLikeRepository.findByIdAndUserId(studyId, user.getId());

        if (existingLike.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_LIKE);
        }

        StudyLike studyLike = new StudyLike(study, user);
        StudyLike saveStudyLike = studyLikeRepository.save(studyLike);

        return studyLikeRepository.countLikeById(studyId);
    }
}
