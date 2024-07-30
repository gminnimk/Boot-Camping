package com.sparta.studytrek.domain.question.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.service.UserService;
import com.sparta.studytrek.domain.question.dto.QuestionRequestDto;
import com.sparta.studytrek.domain.question.dto.QuestionResponseDto;
import com.sparta.studytrek.domain.question.entity.Question;
import com.sparta.studytrek.domain.question.repository.QuestionRepository;
//import com.sparta.studytrek.domain.question.repository.QuestionRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserService userService;

    /**
     * 질문 작성
     *
     * @param requestDto 질문 등록 요청 데이터
     * @param user       요청한 유저의 정보
     * @return 질문 응답 데이터
     */
    public QuestionResponseDto createQuestion(QuestionRequestDto requestDto, User user) {

        Question question = new Question(requestDto, user);
        Question createQuestion = questionRepository.save(question);

        return new QuestionResponseDto(createQuestion);
    }

    /**
     * 질문 수정
     *
     * @param id            질문 ID
     * @param requestDto    질문 수정 요청 데이터
     * @param user          요청한 유저의 정보
     * @return  질문 응답 데이터
     */
    @Transactional
    public QuestionResponseDto updateQuestion(Long id, QuestionRequestDto requestDto, User user) {
        Question question = findByQuestionId(id);

        question.update(requestDto);
        return new QuestionResponseDto(question);

    }

    /**
     * 질문 삭제
     *
     * @param id   질문 ID
     * @param user 요청한 유저의 정보
     */
    public void deleteQuestion(Long id, User user) {
        Question question = findByQuestionId(id);

        questionRepository.delete(question);
    }

    /**
     * 질문 찾기
     *
     * @param id 질문 ID
     * @return 해당 질문 정보
     */
    private Question findByQuestionId(Long id) {
        return questionRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_QUESTION));
    }
}
