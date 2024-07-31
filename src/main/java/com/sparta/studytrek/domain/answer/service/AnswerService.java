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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 답변 수정
     *
     * @param questionId    질문 ID
     * @param answerId      답변 ID
     * @param requestDto    답변 수정 요청 데이터
     * @param user  요청한 유저의 정보
     * @return  답변 응답 데이터
     */
    @Transactional
    public AnswerResponseDto updateAnswer(Long questionId, Long answerId, AnswerRequestDto requestDto, User user) {
        Question question = findById(questionId);
        Answer answer = findByAnswerId(answerId);
        answer.update(requestDto);
        return new AnswerResponseDto(answer);
    }

    /**
     * 답변 삭제
     *
     * @param questionId    질문 ID
     * @param answerId      답변 ID
     * @param user  요청한 유저의 정보
     */
    public void deleteAnswer(Long questionId, Long answerId, User user) {
        Question question = findById(questionId);
        Answer answer = findByAnswerId(answerId);
        answerRepository.delete(answer);
    }

    /**
     * 답변 전체 조회
     *
     * @param pageable  페이지 정보
     * @return  답변 전체 목록
     */
    public Page<AnswerResponseDto> getAnswers(Pageable pageable) {
        Page<Answer> answerPage = answerRepository.findByAllByOrderByCreatedAtDesc(pageable);
        return answerPage.map(AnswerResponseDto::new);
    }

    /**
     * 답변 단건 조회
     *
     * @param questionId    질문 ID
     * @param answerId      답변 ID
     * @return  해당 답변의 응답 데이터
     */
    public AnswerResponseDto getAnswer(Long questionId, Long answerId) {
        Answer answer = findByAnswerId(answerId);
        return new AnswerResponseDto(answer);
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

    /**
     * 답변 찾기
     *
     * @param answerId  답변 ID
     * @return  해당 답변의 정보
     */
    private Answer findByAnswerId(Long answerId) {
        return answerRepository.findById(answerId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_ANSWER));
    }

}
