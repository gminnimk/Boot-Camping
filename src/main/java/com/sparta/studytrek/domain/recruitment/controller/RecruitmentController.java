package com.sparta.studytrek.domain.recruitment.controller;

import com.sparta.studytrek.aop.RecruitmentRoleCheck;
import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.common.ResponseText;
import com.sparta.studytrek.domain.camp.entity.Camp;
import com.sparta.studytrek.domain.camp.service.CampService;
import com.sparta.studytrek.domain.recruitment.dto.RecruitmentRequestDto;
import com.sparta.studytrek.domain.recruitment.dto.RecruitmentResponseDto;
import com.sparta.studytrek.domain.recruitment.service.RecruitmentService;
import com.sparta.studytrek.security.UserDetailsImpl;
import java.io.IOException;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/camps")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    /**
     * 모집글 작성 API
     *
     * @param requestDto  모집글 작성에 필요한 데이터
     * @param imageFile   모집글에 첨부할 이미지 파일
     * @param userDetails 인증된 사용자 정보
     * @return 작성된 모집글에 대한 응답 데이터
     * @throws IOException              이미지 파일 처리 중 발생할 수 있는 예외
     * @throws IllegalArgumentException 잘못된 입력 데이터가 주어진 경우 발생하는 예외
     */
    @PostMapping
    @RecruitmentRoleCheck
    public ResponseEntity<ApiResponse> createRecruitment(
        @RequestPart("data") RecruitmentRequestDto requestDto,
        @RequestPart("imageFile") MultipartFile imageFile,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        try {
            RecruitmentResponseDto responseDto = recruitmentService.createRecruitment(requestDto,
                imageFile, userDetails.getUser());
            ApiResponse response = ApiResponse.builder()
                .msg(ResponseText.RECRUITMENT_CREATE_SUCCESS.getMsg())
                .statuscode(String.valueOf(HttpStatus.CREATED.value()))
                .data(responseDto)
                .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                .msg(e.getMessage())
                .statuscode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .build());
        }
    }

    /**
     * 모집글 수정 API
     *
     * @param id          수정할 모집글의 ID
     * @param requestDto  모집글 수정에 필요한 데이터
     * @param imageFile   수정된 모집글에 첨부할 새로운 이미지 파일
     * @param userDetails 인증된 사용자 정보
     * @return 수정된 모집글에 대한 응답 데이터
     * @throws IOException              이미지 파일 처리 중 발생할 수 있는 예외
     * @throws IllegalArgumentException 잘못된 입력 데이터가 주어진 경우 발생하는 예외
     */
    @PutMapping("/{id}")
    @RecruitmentRoleCheck
    public ResponseEntity<ApiResponse> updateRecruitment(
        @PathVariable Long id,
        @RequestPart("data") RecruitmentRequestDto requestDto,
        @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        try {
            RecruitmentResponseDto responseDto = recruitmentService.updateRecruitment(id,
                requestDto, imageFile, userDetails.getUser());
            ApiResponse response = ApiResponse.builder()
                .msg(ResponseText.RECRUITMENT_UPDATE_SUCCESS.getMsg())
                .statuscode(String.valueOf(HttpStatus.OK.value()))
                .data(responseDto)
                .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                .msg(e.getMessage())
                .statuscode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .build());
        }
    }

    /**
     * 모집글 삭제
     *
     * @param id          모집글 ID
     * @param userDetails 요청한 유저의 정보
     */
    @DeleteMapping("/{id}")
    @RecruitmentRoleCheck
    public ResponseEntity<ApiResponse> deleteRecruitment(@PathVariable Long id,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        recruitmentService.deleteRecruitment(id, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.RECRUITMENT_DELETE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.NO_CONTENT.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 모집글 전체 조회
     *
     * @param pageable 페이지 정보
     * @return 모집글 전체 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAllRecruitments(Pageable pageable) {
        Page<RecruitmentResponseDto> responseDtos = recruitmentService.getAllRecruitments(pageable);
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.RECRUITMENT_GET_ALL_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDtos)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 모집글 단건 조회 API
     *
     * @param id 모집글 ID
     * @return 해당 모집글의 응답 데이터
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getRecruitment(@PathVariable Long id) {
        RecruitmentResponseDto responseDto = recruitmentService.getRecruitmentWithSummary(id);
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.RECRUITMENT_GET_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 모집글 필터링 API
     *
     * @param trek  모집글의 트랙 유형 리스트. 이 리스트에 포함된 유형으로 모집글을 필터링
     * @param place 모집글의 장소 리스트.
     * @param cost  모집글의 비용 리스트.
     * @return 필터링된 모집글 리스트.
     */
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse> filterRecruitments(
        @RequestParam(required = false) List<String> trek,
        @RequestParam(required = false) List<String> place,
        @RequestParam(required = false) List<String> cost) {
        List<RecruitmentResponseDto> responseDtos = recruitmentService.getFilteredRecruitments(trek,
            place, cost);
        ApiResponse response = ApiResponse.builder()
            .msg("Filtered recruitments fetched successfully")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDtos)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
