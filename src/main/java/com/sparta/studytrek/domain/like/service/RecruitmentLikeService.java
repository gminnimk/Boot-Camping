package com.sparta.studytrek.domain.like.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.like.entity.RecruitmentLike;
import com.sparta.studytrek.domain.like.repository.RecruitmentLikeRepository;
import com.sparta.studytrek.domain.recruitment.entity.Recruitment;
import com.sparta.studytrek.domain.recruitment.repository.RecruitmentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentLikeService {

    private final RecruitmentLikeRepository recruitmentLikeRepository;
    private final RecruitmentRepository recruitmentRepository;

    @Transactional
    public int recruitLike(Long campId, User user) {
        Recruitment recruitment = recruitmentRepository.findById(campId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_RECRUITMENT));

        Optional<RecruitmentLike> existingLike = recruitmentLikeRepository.findByIdAndUserId(campId, user.getId());

        if(existingLike.isPresent()){
            throw new CustomException(ErrorCode.ALREADY_LIKE);
        }

        RecruitmentLike recruitmentLike = new RecruitmentLike(recruitment, user);
        RecruitmentLike saveRecruitmentLike = recruitmentLikeRepository.save(recruitmentLike);
        return recruitmentLikeRepository.countLikeById(campId);
    }
}
