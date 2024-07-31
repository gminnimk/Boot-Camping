package com.sparta.studytrek.domain.like.controller;


import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.like.service.StudyLikeService;
import com.sparta.studytrek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studied/{studyId}/likes")
public class StudyLikeController {

    private final StudyLikeService studyLikeService;

    @PostMapping
    public ResponseEntity<ApiResponse> studyLike(@PathVariable Long studyId,
        @AuthenticationPrincipal UserDetailsImpl userDetails){

        int studyAllLike = studyLikeService.studyLike(studyId, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("해당 스터디 모집글에 좋아요 성공 : "  + studyAllLike)
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> studyUnlike(@PathVariable Long studyId,
        @AuthenticationPrincipal UserDetailsImpl userDetails){

        int studyAllLike = studyLikeService.studyUnlike(studyId, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("해당 스터디 모집글에 좋아요 취소 성공 : "  + studyAllLike)
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
