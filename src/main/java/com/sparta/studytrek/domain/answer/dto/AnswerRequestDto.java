package com.sparta.studytrek.domain.answer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerRequestDto {

    @NotBlank(message = "답변은 필수 입력입니다.")
    private String content;

}
