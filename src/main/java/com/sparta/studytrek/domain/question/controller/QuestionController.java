package com.sparta.studytrek.domain.question.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.question.dto.QuestionRequestDto;
import com.sparta.studytrek.domain.question.dto.QuestionResponseDto;
import com.sparta.studytrek.domain.question.service.QuestionService;
import com.sparta.studytrek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    /**
     * 질문 작성 API
     *
     * @param requestDto  질문 작성 데이터
     * @param userDetails 인증된 유저 정보
     * @return 질문 작성 응답 데이터
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createQuestion(@RequestBody QuestionRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        QuestionResponseDto responseDto = questionService.createQuestion(requestDto,
            userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("질문 작성 성공")
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
