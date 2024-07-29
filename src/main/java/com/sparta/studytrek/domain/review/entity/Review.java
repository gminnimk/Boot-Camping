package com.sparta.studytrek.domain.review.entity;

import com.sparta.studytrek.common.Timestamped;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.camp.entity.Camp;
import com.sparta.studytrek.domain.comment.entity.ReviewComment;
import com.sparta.studytrek.domain.like.entity.ReviewLike;
import com.sparta.studytrek.domain.review.dto.ReviewRequestDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 유저 정보

    @Column(nullable = false)
    private String title;  // 리뷰 제목

    @Column(nullable = false)
    private String content;  // 리뷰 내용

    @Column(nullable = false)
    private int scope;  // 리뷰 별점

    @Column(nullable = false)
    private String category;  // 카테고리

    @Column(nullable = false)
    private String trek;  // 트랙

    @ManyToOne
    @JoinColumn(name = "camp_id", nullable = false)
    private Camp camp;  // 캠프 정보

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewComment> comments = new ArrayList<>();

    public Review(ReviewRequestDto requestDto, User user, Camp camp) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.scope = requestDto.getScope();
        this.category = requestDto.getCategory();
        this.trek = requestDto.getTrek();
        this.user = user;
        this.camp = camp;
    }

    public void updateReview(ReviewRequestDto requestDto, Camp camp) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.scope = requestDto.getScope();
        this.category = requestDto.getCategory();
        this.trek = requestDto.getTrek();
        this.camp = camp;
    }

    public Review(User user, String title, String content, int scope, String trek,
        String category) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.scope = scope;
        this.trek = trek;
        this.category = category;
    }
}
