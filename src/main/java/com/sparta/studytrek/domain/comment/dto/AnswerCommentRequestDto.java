package com.sparta.studytrek.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AnswerCommentRequestDto {
    @NotBlank(message = "내용은 필수 입력입니다.")
    private String content;
}
