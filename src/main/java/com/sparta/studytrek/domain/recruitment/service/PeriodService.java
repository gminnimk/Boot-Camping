package com.sparta.studytrek.domain.recruitment.service;

import com.sparta.studytrek.domain.recruitment.dto.ParticipatePeriodResponseDto;
import com.sparta.studytrek.domain.recruitment.dto.RecruitPeriodResponseDto;
import com.sparta.studytrek.domain.recruitment.repository.RecruitmentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PeriodService {

    private final RecruitmentRepository recruitmentRepository;

    /**
     * 부트캠프 모집 기간 조회
     *
     * @return 해당 부트캠프의 모집 기간 데이터
     */
    public List<RecruitPeriodResponseDto> getRecruitments() {
        List<RecruitPeriodResponseDto> recruitments = recruitmentRepository.findAllRecruitPeriods();
        return recruitments;
    }

    /**
     * 부트캠프 참여 기간 조회
     *
     * @return 해당 부트캠프의 참여 기간 데이터
     */
    public List<ParticipatePeriodResponseDto> getParticipates() {
        List<ParticipatePeriodResponseDto> recruitments = recruitmentRepository.findAllParticipates();
        return recruitments;
    }
}
