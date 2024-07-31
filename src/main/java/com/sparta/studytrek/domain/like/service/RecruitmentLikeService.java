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

    /**
     * 좋아요 추가
     *
     * @param recruitmentId 부트캠프 모집글 ID
     * @param user  유저 정보
     * @return  좋아요 응답 데이터
     */
    @Transactional
    public int recruitLike(Long recruitmentId, User user) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_RECRUITMENT));

        Optional<RecruitmentLike> existingLike = recruitmentLikeRepository.findByRecruitmentIdAndUserId(recruitmentId, user.getId());

        if(existingLike.isPresent()){
            throw new CustomException(ErrorCode.ALREADY_LIKE);
        }

        RecruitmentLike recruitmentLike = new RecruitmentLike(recruitment, user);
        recruitmentLikeRepository.save(recruitmentLike);
        return recruitmentLikeRepository.countLikeByRecruitmentId(recruitmentId);
    }

    /**
     * 좋아요 취소
     *
     * @param recruitmentId 부트캠프 모집글 ID
     * @param user  유저 정보
     * @return  좋아요 취소 응답 데이터
     */
    @Transactional
    public int recruitUnlike(Long recruitmentId, User user) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_RECRUITMENT));

        RecruitmentLike recruitmentLike = recruitmentLikeRepository.findByRecruitmentIdAndUserId(recruitmentId, user.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_LIKE));

        recruitmentLikeRepository.delete(recruitmentLike);
        return recruitmentLikeRepository.countLikeByRecruitmentId(recruitmentId);
    }
}
