package com.sparta.studytrek.domain.answer.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.answer.dto.AnswerRequestDto;
import com.sparta.studytrek.domain.answer.dto.AnswerResponseDto;
import com.sparta.studytrek.domain.answer.entity.Answer;
import com.sparta.studytrek.domain.answer.repository.AnswerRepository;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.question.dto.QuestionResponseDto;
import com.sparta.studytrek.domain.question.entity.Question;
import com.sparta.studytrek.domain.question.repository.QuestionRepository;
import com.sparta.studytrek.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    /**
     * 답변 작성
     *
     * @param questionId    질문 ID
     * @param requestDto    답변 작성 요청 데이터
     * @param user          요청한 유저의 정보
     * @return  답변 응답 데이터
     */
    public AnswerResponseDto createAnswer(Long questionId, AnswerRequestDto requestDto, User user) {

        Question question = findById(questionId);
        Answer answer = new Answer(requestDto, question, user);
        Answer savedAnswer = answerRepository.save(answer);
        return new AnswerResponseDto(savedAnswer);
    }

    /**
     * 질문 찾기
     *
     * @param questionId    질문 ID
     * @return  해당 질문의 정보
     */
    private Question findById(Long questionId) {
        return questionRepository.findById(questionId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_QUESTION));
    }
}
