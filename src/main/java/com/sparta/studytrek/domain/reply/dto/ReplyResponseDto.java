package com.sparta.studytrek.domain.reply.dto;

import com.sparta.studytrek.domain.reply.entity.ReviewReply;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReplyResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public ReplyResponseDto(ReviewReply reviewReply) {
        this.id = reviewReply.getId();
        this.content = reviewReply.getContent();
        this.createdAt = reviewReply.getCreatedAt();
        this.updatedAt = reviewReply.getUpdatedAt();
    }
}
