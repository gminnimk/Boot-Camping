package com.sparta.studytrek.domain.answer.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.answer.dto.AnswerRequestDto;
import com.sparta.studytrek.domain.answer.dto.AnswerResponseDto;
import com.sparta.studytrek.domain.answer.service.AnswerService;
import com.sparta.studytrek.domain.question.dto.QuestionResponseDto;
import com.sparta.studytrek.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions/{questionId}/answers")
public class AnswerController {

    private final AnswerService answerService;

    /**
     * 답변 작성 API
     *
     * @param questionId    질문 ID
     * @param requestDto    답변 작성 데이터
     * @param userDetails   인증된 유저 정보
     * @return  답변 작성 응답 데이터
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createAnswer(@PathVariable("questionId") Long questionId,
        @RequestBody AnswerRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        AnswerResponseDto responseDto = answerService.createAnswer(questionId, requestDto,
            userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("답변 작성 성공")
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 답변 수정 APi
     *
     * @param questionId    질문 ID
     * @param answerId      답변 ID
     * @param requestDto    답변 수정 데이터
     * @param userDetails   인증된 유저 정보
     * @return  답변 수정 응답 데이터
     */
    @Transactional
    @PutMapping("/{answerId}")
    public ResponseEntity<ApiResponse> updateAnswer(@PathVariable("questionId") Long questionId,
        @PathVariable("answerId") Long answerId,
        @RequestBody AnswerRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        AnswerResponseDto responseDto = answerService.updateAnswer(questionId, answerId,requestDto, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("답변 수정 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 답변 삭제 API
     *
     * @param questionId    질문 ID
     * @param answerId      답변 ID
     * @param userDetails   인증된 유저 정보
     * @return  답변 삭제 응답 데이터
     */
    @DeleteMapping("/{answerId}")
    public ResponseEntity<ApiResponse> deleteAnswer(@PathVariable("questionId") Long questionId,
        @PathVariable("answerId") Long answerId,
        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        answerService.deleteAnswer(questionId, answerId, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("답변 삭제 성공")
            .statuscode(String.valueOf(HttpStatus.NO_CONTENT.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 답변 전체 조회 API
     *
     * @param questionId  질문 ID
     * @return  답변 전체 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAnswers(@PathVariable Long questionId)
    {
        List<AnswerResponseDto> answers = answerService.getAnswers(questionId);
        ApiResponse response = ApiResponse.builder()
            .msg("답변 전체 조회 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(answers)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 답변 단건 조회API
     *
     * @param questionId    질문 ID
     * @param answerId      답변 ID
     * @return  해당 답변의 응답 데이터
     */
    @GetMapping("{answerId}")
    public ResponseEntity<ApiResponse> getAnswer(@PathVariable("questionId") Long questionId,
        @PathVariable("answerId") Long answerId)
    {
        AnswerResponseDto responseDto = answerService.getAnswer(questionId, answerId);
        ApiResponse response = ApiResponse.builder()
            .msg("답변 단건 조회 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
