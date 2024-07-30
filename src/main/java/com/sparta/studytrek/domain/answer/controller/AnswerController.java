package com.sparta.studytrek.domain.answer.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.answer.dto.AnswerRequestDto;
import com.sparta.studytrek.domain.answer.dto.AnswerResponseDto;
import com.sparta.studytrek.domain.answer.service.AnswerService;
import com.sparta.studytrek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
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
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
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
        @AuthenticationPrincipal UserDetailsImpl userDetails){

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
        @AuthenticationPrincipal UserDetailsImpl userDetails){

        answerService.deleteAnswer(questionId, answerId, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("답변 삭제 성공")
            .statuscode(String.valueOf(HttpStatus.NO_CONTENT.value()))
            .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }


}
