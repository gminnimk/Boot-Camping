package com.sparta.studytrek.domain.reply.entity;

import com.sparta.studytrek.common.Timestamped;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.comment.entity.ReviewComment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(nullable = false)
    private String content;

    public ReviewReply(ReviewComment reviewComment, User user, String content) {
        this.reviewComment = reviewComment;
        this.user = user;
        this.content = content;
    }
}
