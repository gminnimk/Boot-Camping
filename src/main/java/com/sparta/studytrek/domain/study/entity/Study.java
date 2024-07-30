package com.sparta.studytrek.domain.study.entity;

import com.sparta.studytrek.common.Timestamped;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.comment.entity.StudyComment;
import com.sparta.studytrek.domain.like.entity.StudyLike;
import com.sparta.studytrek.domain.reply.entity.StudyReply;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Study extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자의 역할 정보는 필요할 때 User 엔티티를 통해 접근할 수 있음
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int maxCount;

    @Column(nullable = false)
    private String periodExpected;

    @Column(nullable = false)
    private String cycle;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyReply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyLike> likes = new ArrayList<>();


    public Study(User user, String title, String category, String content, int maxCount,
        String periodExpected, String cycle) {
        this.user = user;
        this.title = title;
        this.category = category;
        this.content = content;
        this.maxCount = maxCount;
        this.periodExpected = periodExpected;
        this.cycle = cycle;
    }

    public void updateStudy(String title, String category, String content, int maxCount,
        String periodExpected, String cycle) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.maxCount = maxCount;
        this.periodExpected = periodExpected;
        this.cycle = cycle;
    }
}