package com.sparta.studytrek.domain.review.entity;

import com.sparta.studytrek.common.Timestamped;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.comment.entity.ReviewComment;
import com.sparta.studytrek.domain.like.entity.ReviewLike;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Review extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String scope;

    @Column(nullable = false)
    private String trek;

    @Column(nullable = false)
    private String category;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewComment> comments = new ArrayList<>();

    public Review(User user, String title, String content, String scope, String trek, String category) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.scope = scope;
        this.trek = trek;
        this.category = category;
    }
}
