package com.sparta.studytrek.domain.like.entity;

import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.study.entity.Study;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "study_like")
@Getter
@NoArgsConstructor
public class StudyLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    public StudyLike(Study study, User user) {
        this.study = study;
        this.user = user;
    }
}
