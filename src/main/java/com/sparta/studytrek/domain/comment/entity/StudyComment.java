package com.sparta.studytrek.domain.comment.entity;

import com.sparta.studytrek.common.Timestamped;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.reply.entity.StudyReply;
import com.sparta.studytrek.domain.study.entity.Study;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class StudyComment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "studyComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyReply> replies = new ArrayList<>();

    public StudyComment(Study study, User user, String content) {
        this.study = study;
        this.user = user;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
