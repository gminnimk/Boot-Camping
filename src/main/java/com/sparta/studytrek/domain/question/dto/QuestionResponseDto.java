package com.sparta.studytrek.domain.question.dto;

import com.sparta.studytrek.domain.question.entity.Question;
import java.time.LocalDateTime;

public class QuestionResponseDto {

    private String title;
    private String content;
    private String category;
    private LocalDateTime createdAt;

    public QuestionResponseDto(Question createQuestion) {
        this.title = createQuestion.getTitle();
        this.content = createQuestion.getContent();
        this.category = createQuestion.getCategory();
        this.createdAt = createQuestion.getCreatedAt();
    }

}
