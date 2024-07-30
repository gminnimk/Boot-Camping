package com.sparta.studytrek.domain.study.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.study.dto.StudyRequestDto;
import com.sparta.studytrek.domain.study.dto.StudyResponseDto;
import com.sparta.studytrek.domain.study.service.StudyService;
import com.sparta.studytrek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;


    /**
     * 스터디 모집글 작성 API
     *
     * @param request     스터디 모집글 작성 데이터
     * @param userDetails 인증된 유저 정보
     * @return 스터디 모집글 작성 응답 데이터
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StudyResponseDto>> createStudy(
        @RequestBody StudyRequestDto request,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StudyResponseDto responseDtos = studyService.createStudy(request, userDetails.getUser());
        ApiResponse<StudyResponseDto> apiResponse = ApiResponse.<StudyResponseDto>builder()
            .msg("스터디 모집글 작성 성공")
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(responseDtos)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /**
     * 스터디 모집글 전체 조회 (페이징 처리) API
     *
     * @param pageable 페이지 정보
     * @return 스터디 모집글 페이징 조회 데이터
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<StudyResponseDto>>> getAllStudies(Pageable pageable) {
        Page<StudyResponseDto> response = studyService.getAllStudies(pageable);
        ApiResponse<Page<StudyResponseDto>> apiResponse = ApiResponse.<Page<StudyResponseDto>>builder()
            .msg("스터디 모집글 전체 조회 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(response)
            .build();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 스터디 모집글 단건 조회 API
     *
     * @param studyId 조회할 스터디 모집글 ID
     * @return 스터디 모집글 응답 데이터
     */
    @GetMapping("/{studyId}")
    public ResponseEntity<ApiResponse<StudyResponseDto>> getStudy(@PathVariable Long studyId) {
        StudyResponseDto study = studyService.getStudy(studyId);
        ApiResponse<StudyResponseDto> apiResponse = ApiResponse.<StudyResponseDto>builder()
            .msg("스터디 모집글 단건 조회 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(study)
            .build();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 스터디 모집글 수정 API
     *
     * @param studyId     수정할 스터디 모집글 ID
     * @param request     스터디 모집글 수정 데이터
     * @param userDetails 인증된 유저 정보
     * @return 수정된 스터디 모집글 응답 데이터
     */
    @PutMapping("/{studyId}")
    public ResponseEntity<ApiResponse<StudyResponseDto>> updateStudy(@PathVariable Long studyId,
        @RequestBody StudyRequestDto request,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StudyResponseDto updatedStudy = studyService.updateStudy(studyId, request,
            userDetails.getUser());
        ApiResponse<StudyResponseDto> apiResponse = ApiResponse.<StudyResponseDto>builder()
            .msg("스터디 모집글 수정 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(updatedStudy)
            .build();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 스터디 모집글 삭제 API
     *
     * @param studyId     삭제할 스터디 모집글 ID
     * @param userDetails 인증된 유저 정보
     * @return 삭제 결과 응답 데이터
     */
    @DeleteMapping("/{studyId}")
    public ResponseEntity<ApiResponse<Void>> deleteStudy(@PathVariable Long studyId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        studyService.deleteStudy(studyId, userDetails.getUser());
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
            .msg("스터디 모집글 삭제 성공")
            .statuscode(String.valueOf(HttpStatus.NO_CONTENT.value()))
            .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
    }
}