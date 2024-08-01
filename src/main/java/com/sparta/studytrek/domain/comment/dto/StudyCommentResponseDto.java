package com.sparta.studytrek.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyCommentResponseDto {

    private Long id;
    private String content;
    private String createdAt;
    private String modifiedAt;
}
