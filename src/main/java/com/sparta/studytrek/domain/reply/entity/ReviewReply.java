package com.sparta.studytrek.domain.reply.entity;

import com.sparta.studytrek.common.Timestamped;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.comment.entity.ReviewComment;
import com.sparta.studytrek.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReviewReply extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_comment_id", nullable = false)
    private ReviewComment reviewComment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(nullable = false)
    private String content;

    public ReviewReply(ReviewComment reviewComment, User user, Review review, String content) {
        this.reviewComment = reviewComment;
        this.user = user;
        this.review = review;
        this.content = content;
    }
}
