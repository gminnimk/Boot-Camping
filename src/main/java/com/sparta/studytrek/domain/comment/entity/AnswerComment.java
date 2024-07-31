package com.sparta.studytrek.domain.comment.entity;

import com.sparta.studytrek.common.Timestamped;
import com.sparta.studytrek.domain.answer.dto.AnswerRequestDto;
import com.sparta.studytrek.domain.answer.entity.Answer;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.comment.dto.AnswerCommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class AnswerComment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String content;

    public AnswerComment(Answer answer, User user, AnswerCommentRequestDto requestDto){
        this.content = requestDto.getContent();
        this.answer = answer;
        this.user = user;
    }

    public void update(AnswerCommentRequestDto requestDto){
        this.content = requestDto.getContent();
    }

    public AnswerComment(Answer answer, User user, String content) {
        this.answer = answer;
        this.user = user;
        this.content = content;
    }
}
