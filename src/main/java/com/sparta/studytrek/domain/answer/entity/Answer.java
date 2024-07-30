package com.sparta.studytrek.domain.answer.entity;

import com.sparta.studytrek.common.Timestamped;
import com.sparta.studytrek.domain.answer.dto.AnswerRequestDto;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.comment.entity.AnswerComment;
import com.sparta.studytrek.domain.question.entity.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Answer extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerComment> comments = new ArrayList<>();

    @Column(nullable = false)
    private String content;

    public Answer(AnswerRequestDto requestDto, User user){
        this.content = requestDto.getContent();
        this.user = user;
    }

    public Answer(Question question, User user, String content) {
        this.question = question;
        this.user = user;
        this.content = content;
    }
}
