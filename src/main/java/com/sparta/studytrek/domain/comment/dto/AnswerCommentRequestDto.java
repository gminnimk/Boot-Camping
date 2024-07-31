package com.sparta.studytrek.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCommentRequestDto {
    @NotBlank(message = "내용은 필수 입력입니다.")
    private String content;
}
