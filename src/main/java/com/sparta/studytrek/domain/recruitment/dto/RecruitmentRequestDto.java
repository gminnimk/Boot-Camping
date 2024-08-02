package com.sparta.studytrek.domain.recruitment.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class RecruitmentRequestDto {
    private String title;
    private String process;
    private String content;
    private String place;
    private String cost;
    private String trek;
    private String level;
    private String classTime;
    private LocalDate campStart;
    private LocalDate campEnd;
    private LocalDate recruitStart;
    private LocalDate recruitEnd;
    private String campName;
}
