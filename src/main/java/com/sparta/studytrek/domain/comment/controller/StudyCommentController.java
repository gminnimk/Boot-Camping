package com.sparta.studytrek.domain.comment.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.common.ResponseText;
import com.sparta.studytrek.domain.comment.dto.StudyCommentRequestDto;
import com.sparta.studytrek.domain.comment.dto.StudyCommentResponseDto;
import com.sparta.studytrek.domain.comment.service.StudyCommentService;
import com.sparta.studytrek.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/studies/{studyId}/comments")
@RequiredArgsConstructor
public class StudyCommentController {

    private final StudyCommentService studyCommentService;

    /**
     * 스터디 모집글에 댓글을 작성하는 API
     *
     * @param studyId     댓글을 작성할 스터디 모집글의 ID
     * @param request     댓글 작성 데이터
     * @param userDetails 인증된 유저 정보
     * @return 작성된 댓글 응답 데이터
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StudyCommentResponseDto>> createComment(
        @PathVariable Long studyId, @Valid @RequestBody StudyCommentRequestDto request,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        StudyCommentResponseDto responseDto = studyCommentService.createComment(studyId, request,
            userDetails.getUser());
        ApiResponse<StudyCommentResponseDto> apiResponse = ApiResponse.<StudyCommentResponseDto>builder()
            .msg(ResponseText.COMMENT_CREATE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /**
     * 스터디 모집글의 모든 댓글을 조회하는 API
     *
     * @param studyId 댓글을 조회할 스터디 모집글의 ID
     * @return 스터디 모집글의 댓글 전체 조회 데이터
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudyCommentResponseDto>>> getAllComments(
        @PathVariable Long studyId)
    {
        List<StudyCommentResponseDto> response = studyCommentService.getComments(studyId);
        ApiResponse<List<StudyCommentResponseDto>> apiResponse = ApiResponse.<List<StudyCommentResponseDto>>builder()
            .msg(ResponseText.COMMENT_GET_ALL_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(response)
            .build();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 특정 댓글을 조회하는 API
     *
     * @param studyId   댓글을 조회할 스터디 모집글의 ID
     * @param commentId 조회할 댓글의 ID
     * @return 특정 댓글 응답 데이터
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponse<StudyCommentResponseDto>> getComment(
        @PathVariable Long studyId, @PathVariable Long commentId)
    {
        StudyCommentResponseDto responseDto = studyCommentService.getComment(studyId, commentId);
        ApiResponse<StudyCommentResponseDto> apiResponse = ApiResponse.<StudyCommentResponseDto>builder()
            .msg(ResponseText.COMMENT_GET_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 특정 댓글을 수정하는 API
     *
     * @param studyId     댓글을 수정할 스터디 모집글의 ID
     * @param commentId   수정할 댓글의 ID
     * @param request     댓글 수정 데이터
     * @param userDetails 인증된 유저 정보
     * @return 수정된 댓글 응답 데이터
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<StudyCommentResponseDto>> updateComment(
        @PathVariable Long studyId, @PathVariable Long commentId,
        @Valid @RequestBody StudyCommentRequestDto request,
        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        StudyCommentResponseDto updatedComment = studyCommentService.updateComment(studyId,
            commentId, request, userDetails.getUser());
        ApiResponse<StudyCommentResponseDto> apiResponse = ApiResponse.<StudyCommentResponseDto>builder()
            .msg(ResponseText.COMMENT_UPDATE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(updatedComment)
            .build();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 특정 댓글을 삭제하는 API
     *
     * @param studyId     댓글을 삭제할 스터디 모집글의 ID
     * @param commentId   삭제할 댓글의 ID
     * @param userDetails 인증된 유저 정보
     * @return 삭제 결과 응답 데이터
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long studyId,
        @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        studyCommentService.deleteComment(studyId, commentId, userDetails.getUser());
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
            .msg(ResponseText.COMMENT_DELETE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.NO_CONTENT.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
