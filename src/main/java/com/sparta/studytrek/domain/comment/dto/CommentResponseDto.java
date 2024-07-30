package com.sparta.studytrek.domain.comment.dto;

import com.sparta.studytrek.domain.comment.entity.ReviewComment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public CommentResponseDto(ReviewComment reviewComment) {
        this.content = reviewComment.getContent();
        this.createdAt = reviewComment.getCreatedAt();
        this.updatedAt = reviewComment.getUpdatedAt();
    }
}
