package com.sparta.studytrek.domain.review.dto;

import com.sparta.studytrek.domain.review.entity.Review;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReviewResponseDto {
    private Long id;
    private String title;
    private String content;
    private String campName;
    private int scope;
    private String category;
    private String trek;
    private LocalDateTime createdAt;

    public ReviewResponseDto(Review creatReview) {
        this.id = creatReview.getId();
        this.title = creatReview.getTitle();
        this.content = creatReview.getContent();
        this.scope = creatReview.getScope();
        this.category = creatReview.getCategory();
        this.trek = creatReview.getTrek();
        this.createdAt = creatReview.getCreatedAt();
        this.campName = creatReview.getCamp().getName();
    }
}
