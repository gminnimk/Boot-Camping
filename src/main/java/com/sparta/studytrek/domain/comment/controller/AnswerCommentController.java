package com.sparta.studytrek.domain.comment.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.comment.dto.AnswerCommentRequestDto;
import com.sparta.studytrek.domain.comment.dto.AnswerCommentResponseDto;
import com.sparta.studytrek.domain.comment.service.AnswerCommentService;
import com.sparta.studytrek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions/{questionId}/answers/{answerId}/comments")
public class AnswerCommentController {

    private final AnswerCommentService answerCommentService;

    /**
     * 질문에 대한 답변의 댓글 작성 API
     *
     * @param questionId    질문 ID
     * @param answerId      답변 ID
     * @param requestDto    댓글 작성 데이터
     * @param userDetails   인증된 유저 정보
     * @return  댓글 작성 응답 데이터
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createAnswerComment(@PathVariable("questionId") Long questionId,
        @PathVariable("answerId") Long answerId,
        @RequestBody AnswerCommentRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        AnswerCommentResponseDto responseDto = answerCommentService.createAnswerComment(questionId, answerId, requestDto, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("댓글 작성 성공")
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
