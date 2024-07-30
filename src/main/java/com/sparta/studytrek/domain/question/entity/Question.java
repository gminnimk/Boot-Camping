package com.sparta.studytrek.domain.question.entity;

import com.sparta.studytrek.domain.answer.entity.Answer;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.common.Timestamped;
import com.sparta.studytrek.domain.question.dto.QuestionRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Question extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String open;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public Question(QuestionRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.category = requestDto.getCategory();
        this.user = user;
    }

    public Question(String title, String content, String open, User user) {
        this.title = title;
        this.content = content;
        this.open = open;
        this.user = user;
    }
}
