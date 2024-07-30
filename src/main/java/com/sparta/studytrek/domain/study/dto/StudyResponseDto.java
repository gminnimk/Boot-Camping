package com.sparta.studytrek.domain.study.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyResponseDto {

    private Long id;
    private String title;
    private String content;
    private String category;
    private int maxCount;
    private String periodExpected;
    private String cycle;
    private String createdAt;
    private String modifiedAt;
}
