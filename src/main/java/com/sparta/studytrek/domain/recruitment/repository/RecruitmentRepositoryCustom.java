package com.sparta.studytrek.domain.recruitment.repository;

import com.sparta.studytrek.domain.recruitment.dto.ParticipatePeriodResponseDto;
import com.sparta.studytrek.domain.recruitment.dto.RecruitPeriodResponseDto;
import java.util.List;

public interface RecruitmentRepositoryCustom {
    List<RecruitPeriodResponseDto> findAllRecruitPeriods();
    List<ParticipatePeriodResponseDto> findAllParticipates();
}
