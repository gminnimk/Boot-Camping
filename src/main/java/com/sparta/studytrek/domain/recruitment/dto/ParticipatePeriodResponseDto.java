package com.sparta.studytrek.domain.recruitment.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ParticipatePeriodResponseDto {
    private Long id;
    private String title;
    private LocalDate campStart;
    private LocalDate campEnd;

    public ParticipatePeriodResponseDto(Long id, String title, LocalDate campStart,
        LocalDate campEnd) {
        this.id = id;
        this.title = title;
        this.campStart = campStart;
        this.campEnd = campEnd;
    }
}
