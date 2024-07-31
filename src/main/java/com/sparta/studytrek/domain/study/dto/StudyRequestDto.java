package com.sparta.studytrek.domain.study.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyRequestDto {

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    private String content;

    @NotBlank(message = "카테고리는 필수입니다")
    private String category;

    @NotNull(message = "최대 인원수는 필수입니다")
    @Min(value = 1, message = "최대 인원수는 1 이상이어야 합니다")
    private int maxCount;

    @NotBlank(message = "예상 기간은 필수입니다")
    private String periodExpected;

    @NotBlank(message = "모임 주기는 필수입니다")
    private String cycle;
}