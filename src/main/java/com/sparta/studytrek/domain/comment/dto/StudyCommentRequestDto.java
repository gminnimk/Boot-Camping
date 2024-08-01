package com.sparta.studytrek.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class StudyCommentRequestDto {

    @NotBlank(message = "내용은 필수입니다")
    private String content;
}