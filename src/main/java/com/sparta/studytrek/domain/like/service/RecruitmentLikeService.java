package com.sparta.studytrek.domain.like.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.like.repository.RecruitmentLikeRepository;
import com.sparta.studytrek.domain.recruitment.entity.Recruitment;
import com.sparta.studytrek.domain.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentLikeService {

    private final RecruitmentLikeRepository recruitmentLikeRepository;
    private final RecruitmentRepository recruitmentRepository;

    public int recruitLike(Long campId, User user) {
        Recruitment recruitment = recruitmentRepository.findById(campId)
            .orElseThrow(() -> new CustomException(ErrorCode.))
    }
}
