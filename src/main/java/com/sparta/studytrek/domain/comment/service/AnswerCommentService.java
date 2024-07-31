package com.sparta.studytrek.domain.comment.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.answer.entity.Answer;
import com.sparta.studytrek.domain.answer.repository.AnswerRepository;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.comment.dto.AnswerCommentRequestDto;
import com.sparta.studytrek.domain.comment.dto.AnswerCommentResponseDto;
import com.sparta.studytrek.domain.comment.entity.AnswerComment;
import com.sparta.studytrek.domain.comment.repository.AnswerCommentRepository;
import com.sparta.studytrek.domain.question.entity.Question;
import com.sparta.studytrek.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerCommentService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final AnswerCommentRepository answerCommentRepository;

    /**
     * 질문에 대한 답변의 댓글 작성
     *
     * @param questionId    질문 ID
     * @param answerId      답변 ID
     * @param requestDto    댓글 요청 내용
     * @param user          유저 정보
     * @return  질문에 대한 답변의 댓글 응답 데이터
     */
    public AnswerCommentResponseDto createAnswerComment(Long questionId, Long answerId,
        AnswerCommentRequestDto requestDto, User user) {
        Question question = findById(questionId);
        Answer answer = findByAnswerId(answerId);

        AnswerComment answerComment = new AnswerComment(answer, user, requestDto);
        AnswerComment saveComment = answerCommentRepository.save(answerComment);
        return new AnswerCommentResponseDto(saveComment);
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
