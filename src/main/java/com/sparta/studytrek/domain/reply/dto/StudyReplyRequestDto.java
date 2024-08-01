package com.sparta.studytrek.domain.reply.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class StudyReplyRequestDto {

    @NotBlank(message = "내용은 필수입니다")
    private String content;
}
