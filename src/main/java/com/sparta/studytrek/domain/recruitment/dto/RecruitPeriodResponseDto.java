package com.sparta.studytrek.domain.recruitment.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class RecruitPeriodResponseDto {
    private Long id;
    private String title;
    private LocalDate recruitStart;
    private LocalDate recruitEnd;

    public RecruitPeriodResponseDto(Long id, String title, LocalDate recruitStart, LocalDate recruitEnd) {
        this.id = id;
        this.title = title;
        this.recruitStart = recruitStart;
        this.recruitEnd = recruitEnd;
    }
}
