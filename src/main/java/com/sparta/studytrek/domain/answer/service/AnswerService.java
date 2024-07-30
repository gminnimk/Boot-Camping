package com.sparta.studytrek.domain.answer.service;

import com.sparta.studytrek.domain.answer.dto.AnswerRequestDto;
import com.sparta.studytrek.domain.answer.dto.AnswerResponseDto;
import com.sparta.studytrek.domain.answer.repository.AnswerRepository;
import com.sparta.studytrek.domain.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionService questionService;

    public AnswerResponseDto createAnswer(Long questionId, AnswerRequestDto requestDto, User user) {

    }
}
