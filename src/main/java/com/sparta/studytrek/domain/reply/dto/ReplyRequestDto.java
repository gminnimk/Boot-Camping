package com.sparta.studytrek.domain.reply.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReplyRequestDto {
    @NotBlank(message = "댓글 내용은 필수 입력입니다.")
    private String content;
}
