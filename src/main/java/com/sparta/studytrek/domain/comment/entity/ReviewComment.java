package com.sparta.studytrek.domain.comment.entity;

import com.sparta.studytrek.common.Timestamped;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.comment.dto.CommentRequestDto;
import com.sparta.studytrek.domain.reply.entity.ReviewReply;
import com.sparta.studytrek.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ReviewComment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String content;  // 댓글 내용

    @OneToMany(mappedBy = "reviewComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewReply> replies = new ArrayList<>();

    public void updateComment(String content) {
        this.content = content;
    }

    public ReviewComment(Review review, User user, CommentRequestDto requestDto) {
        this.review = review;
        this.user = user;
        this.content = requestDto.getContent();
    }

    public ReviewComment(Review review, User user, String content) {
        this.review = review;
        this.user = user;
        this.content = content;
    }
}
