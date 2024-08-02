package com.sparta.studytrek.domain.recruitment.repository;

import static com.sparta.studytrek.domain.recruitment.entity.QRecruitment.recruitment;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.studytrek.domain.recruitment.dto.ParticipatePeriodResponseDto;
import com.sparta.studytrek.domain.recruitment.dto.RecruitPeriodResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecruitmentRepositoryImpl implements RecruitmentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RecruitPeriodResponseDto> findAllRecruitPeriods() {
        return queryFactory
            .select(Projections.constructor(
                RecruitPeriodResponseDto.class,
                recruitment.id,
                recruitment.title,
                recruitment.recruitStart,
                recruitment.recruitEnd
            ))
            .from(recruitment)
            .fetch();
    }

    @Override
    public List<ParticipatePeriodResponseDto> findAllParticipates() {
        return queryFactory
            .select(Projections.constructor(
                ParticipatePeriodResponseDto.class,
                recruitment.id,
                recruitment.title,
                recruitment.campStart,
                recruitment.campEnd
            ))
            .from(recruitment)
            .fetch();
    }
}
