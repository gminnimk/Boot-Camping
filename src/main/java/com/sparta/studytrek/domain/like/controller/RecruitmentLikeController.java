package com.sparta.studytrek.domain.like.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.like.entity.RecruitmentLike;
import com.sparta.studytrek.domain.like.service.RecruitmentLikeService;
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
@RequestMapping("/api/camps/{campId}/likes")
public class RecruitmentLikeController {

    private final RecruitmentLikeService recruitmentLikeService;

    @PostMapping
    public ResponseEntity<ApiResponse> recruitLike(@PathVariable Long campId,
        @AuthenticationPrincipal UserDetailsImpl userDetails){

        int recruitAllLike = recruitmentLikeService.recruitLike(campId, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("해당 부트캠프 모집글에 좋아요 성공 : "  + recruitAllLike)
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> recruitUnlike(@PathVariable Long campId,
        @AuthenticationPrincipal UserDetailsImpl userDetails){

        int recruitAllLike = recruitmentLikeService.recruitUnlike(campId, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg("해당 부트캠프 모집글에 좋아요 성공 : "  + recruitAllLike)
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
