package com.sparta.studytrek.domain.review.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.review.dto.ReviewRequestDto;
import com.sparta.studytrek.domain.review.dto.ReviewResponseDto;
import com.sparta.studytrek.domain.review.service.ReviewService;
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
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 작성 API
     *
     * @param requestDto  리뷰 작성 데이터
     * @param userDetails 인증된 유저 정보
     * @return 리뷰 작성 응답 데이터
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createReview(@RequestBody ReviewRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponseDto responseDto = reviewService.createReview(requestDto,
            userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("리뷰 작성 성공")
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 리뷰 수정 API
     *
     * @param id          리뷰 ID
     * @param requestDto  리뷰 수정 데이터
     * @param userDetails 인증된 유저 정보
     * @return 리뷰 수정 응답 데이터
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateReview(@PathVariable("id") Long id,
        @RequestBody ReviewRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponseDto responseDto = reviewService.updateReview(id, requestDto,
            userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("리뷰 수정 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 리뷰 삭제 API
     *
     * @param id          리뷰 ID
     * @param userDetails 인증된 유저 정보
     * @return 리뷰 삭제 응답 데이터
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable("id") Long id,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(id, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("리뷰 삭제 성공")
            .statuscode(String.valueOf(HttpStatus.NO_CONTENT.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 리뷰 전체 조회 API
     *
     * @param pageable 페이지 정보
     * @return 리뷰 전체 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAllReviews(Pageable pageable) {
        Page<ReviewResponseDto> responseDtos = reviewService.getAllReviews(pageable);
        ApiResponse response = ApiResponse.builder()
            .msg("리뷰 전체 조회 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDtos)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 리뷰 단건 조회 API
     *
     * @param id 리뷰 ID
     * @return 해당 리뷰의 응답 데이터
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getReview(@PathVariable("id") Long id) {
        ReviewResponseDto responseDto = reviewService.getReview(id);
        ApiResponse response = ApiResponse.builder()
            .msg("리뷰 단건 조회 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
