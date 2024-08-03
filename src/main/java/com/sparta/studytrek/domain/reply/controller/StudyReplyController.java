package com.sparta.studytrek.domain.reply.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.common.ResponseText;
import com.sparta.studytrek.domain.reply.dto.StudyReplyRequestDto;
import com.sparta.studytrek.domain.reply.dto.StudyReplyResponseDto;
import com.sparta.studytrek.domain.reply.service.StudyReplyService;
import com.sparta.studytrek.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studies/{studyId}/comments/{commentId}/replies")
@RequiredArgsConstructor
public class StudyReplyController {

    private final StudyReplyService studyReplyService;

    /**
     * 대댓글을 생성하는 API
     *
     * @param studyId     대댓글을 작성할 스터디 모집글의 ID
     * @param commentId   대댓글을 작성할 댓글의 ID
     * @param request     대댓글 작성 데이터
     * @param userDetails 인증된 유저 정보
     * @return 생성된 대댓글 응답 데이터
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StudyReplyResponseDto>> createReply(
        @PathVariable Long studyId, @PathVariable Long commentId,
        @Valid @RequestBody StudyReplyRequestDto request,
        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        StudyReplyResponseDto responseDto = studyReplyService.createReply(studyId, commentId,
            request, userDetails.getUser());
        ApiResponse<StudyReplyResponseDto> apiResponse = ApiResponse.<StudyReplyResponseDto>builder()
            .msg(ResponseText.REPLY_CREATE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /**
     * 특정 댓글의 모든 대댓글을 조회하는 API
     *
     * @param studyId   대댓글을 조회할 스터디 모집글의 ID
     * @param commentId 대댓글을 조회할 댓글의 ID
     * @return 대댓글 전체 조회 데이터
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudyReplyResponseDto>>> getAllReplies(
        @PathVariable Long studyId, @PathVariable Long commentId)
    {
        List<StudyReplyResponseDto> response = studyReplyService.getReplies(studyId, commentId);
        ApiResponse<List<StudyReplyResponseDto>> apiResponse = ApiResponse.<List<StudyReplyResponseDto>>builder()
            .msg(ResponseText.REPLY_GET_ALL_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(response)
            .build();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 특정 대댓글을 조회하는 API
     *
     * @param studyId   대댓글을 조회할 스터디 모집글의 ID
     * @param commentId 대댓글을 조회할 댓글의 ID
     * @param replyId   조회할 대댓글의 ID
     * @return 특정 대댓글 응답 데이터
     */
    @GetMapping("/{replyId}")
    public ResponseEntity<ApiResponse<StudyReplyResponseDto>> getReply(@PathVariable Long studyId,
        @PathVariable Long commentId, @PathVariable Long replyId)
    {
        StudyReplyResponseDto responseDto = studyReplyService.getReply(commentId, replyId);
        ApiResponse<StudyReplyResponseDto> apiResponse = ApiResponse.<StudyReplyResponseDto>builder()
            .msg(ResponseText.REPLY_GET_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 특정 대댓글을 수정하는 API
     *
     * @param studyId     대댓글을 수정할 스터디 모집글의 ID
     * @param commentId   대댓글을 수정할 댓글의 ID
     * @param replyId     수정할 대댓글의 ID
     * @param request     대댓글 수정 데이터
     * @param userDetails 인증된 유저 정보
     * @return 수정된 대댓글 응답 데이터
     */
    @PutMapping("/{replyId}")
    public ResponseEntity<ApiResponse<StudyReplyResponseDto>> updateReply(
        @PathVariable Long studyId, @PathVariable Long commentId, @PathVariable Long replyId,
        @Valid @RequestBody StudyReplyRequestDto request,
        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        StudyReplyResponseDto updatedReply = studyReplyService.updateReply(commentId, replyId,
            request, userDetails.getUser());
        ApiResponse<StudyReplyResponseDto> apiResponse = ApiResponse.<StudyReplyResponseDto>builder()
            .msg(ResponseText.REPLY_UPDATE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(updatedReply)
            .build();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 특정 대댓글을 삭제하는 API
     *
     * @param studyId     대댓글을 삭제할 스터디 모집글의 ID
     * @param commentId   대댓글을 삭제할 댓글의 ID
     * @param replyId     삭제할 대댓글의 ID
     * @param userDetails 인증된 유저 정보
     * @return 삭제 결과 응답 데이터
     */
    @DeleteMapping("/{replyId}")
    public ResponseEntity<ApiResponse<Void>> deleteReply(@PathVariable Long studyId,
        @PathVariable Long commentId, @PathVariable Long replyId,
        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        studyReplyService.deleteReply(commentId, replyId, userDetails.getUser());
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
            .msg(ResponseText.REPLY_DELETE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.NO_CONTENT.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
