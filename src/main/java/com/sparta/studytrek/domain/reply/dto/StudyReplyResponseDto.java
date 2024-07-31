package com.sparta.studytrek.domain.reply.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyReplyResponseDto {

    private Long id;
    private String content;
    private String createdAt;
    private String modifiedAt;
}
