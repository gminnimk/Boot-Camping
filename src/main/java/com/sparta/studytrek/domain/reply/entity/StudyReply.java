package com.sparta.studytrek.domain.reply.entity;

import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.comment.entity.StudyComment;
import com.sparta.studytrek.domain.study.entity.Study;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class StudyReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "study_comment_id", nullable = false)
    private StudyComment studyComment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Column(nullable = false)
    private String content;

    public StudyReply(StudyComment studyComment, User user, Study study, String content) {
        this.studyComment = studyComment;
        this.user = user;
        this.study = study;
        this.content = content;
    }
}
