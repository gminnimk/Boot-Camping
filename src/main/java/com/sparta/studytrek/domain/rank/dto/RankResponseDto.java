package com.sparta.studytrek.domain.rank.dto;

import com.sparta.studytrek.domain.camp.dto.CampResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RankResponseDto {

    private Long id;
    private CampResponseDto camp; // Camp 정보를 담는 DTO
    private Integer ranking;

    // 주어진 생성자 정의
    public RankResponseDto(Long id, CampResponseDto campDto, Integer ranking) {
        this.id = id;
        this.camp = campDto;
        this.ranking = ranking;
    }
}
