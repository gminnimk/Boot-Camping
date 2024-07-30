package com.sparta.studytrek.domain.recruitment.service;

import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.recruitment.dto.RecruitmentRequestDto;
import com.sparta.studytrek.domain.recruitment.dto.RecruitmentResponseDto;
import com.sparta.studytrek.domain.recruitment.entity.Recruitment;
import com.sparta.studytrek.domain.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;

    /**
     * 모집글 작성
     *
     * @param requestDto 모집글 등록 요청 데이터
     * @param user 요청한 유저의 정보
     * @return 모집글 응답 데이터
     */
    public RecruitmentResponseDto createRecruitment(RecruitmentRequestDto requestDto, User user) {
        Recruitment recruitment = new Recruitment(requestDto, user);
        Recruitment createRecruitment = recruitmentRepository.save(recruitment);

        return new RecruitmentResponseDto(createRecruitment);
    }
}
