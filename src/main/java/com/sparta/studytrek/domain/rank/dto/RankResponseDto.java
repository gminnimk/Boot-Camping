package com.sparta.studytrek.domain.rank.dto;

import com.sparta.studytrek.domain.recruitment.dto.RecruitmentResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RankResponseDto {

    private Long id;
    private Integer ranking;
    private RecruitmentResponseDto recruitment;

    public RankResponseDto(Long id, Integer ranking, RecruitmentResponseDto recruitment) {
        this.id = id;
        this.ranking = ranking;
        this.recruitment = recruitment;
    }
}
