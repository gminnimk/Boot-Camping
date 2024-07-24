package com.sparta.studytrek.domain.study.entity;

import com.sparta.studytrek.common.Timestamped;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.comment.entity.StudyComment;
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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyComment> comments = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    public Study(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
    }
}
