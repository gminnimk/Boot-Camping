package com.sparta.studytrek.domain.answer.dto;

import com.sparta.studytrek.domain.answer.entity.Answer;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AnswerResponseDto {

    private Long id;
    private String content;
    private LocalDateTime createdAt;

    public AnswerResponseDto(Answer createAnswer) {
        this.id = createAnswer.getId();
        this.content = createAnswer.getContent();
        this.createdAt = createAnswer.getCreatedAt();
    }

}
