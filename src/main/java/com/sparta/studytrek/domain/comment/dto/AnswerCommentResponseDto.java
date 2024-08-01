package com.sparta.studytrek.domain.comment.dto;

import com.sparta.studytrek.domain.comment.entity.AnswerComment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AnswerCommentResponseDto {

    private String content;
    private LocalDateTime createdAt;

    public AnswerCommentResponseDto(AnswerComment createAnswerComment) {
        this.content = createAnswerComment.getContent();
        this.createdAt = createAnswerComment.getCreatedAt();
    }

}
