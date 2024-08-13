package com.sparta.studytrek.domain.like.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.common.ResponseText;
import com.sparta.studytrek.domain.camp.service.CampService;
import com.sparta.studytrek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/camps/{campId}/like")
@RequiredArgsConstructor
public class CampLikeController {

    private final CampService campService;

    /**
     * 캠프에 좋아요를 추가하는 API
     *
     * @param campId 캠프 ID
     * @param userDetails 인증된 유저 정보
     * @return 좋아요 수를 포함한 성공 메시지를 담고 있는 ApiResponse 객체를 포함한 응답
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> likeCamp(@PathVariable Long campId,
        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        int campAllLike = campService.likeCamp(campId, userDetails.getUser());
        ApiResponse<String> response = ApiResponse.<String>builder()
            .msg(ResponseText.LIKE_RECRUIT_SUCCESS.format(campAllLike))
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 캠프에 대한 좋아요를 취소하는 API
     *
     * @param campId 캠프 ID
     * @param userDetails 인증된 유저 정보
     * @return 좋아요 수를 포함한 성공 메시지를 담고 있는 ApiResponse 객체를 포함한 응답
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> unlikeCamp(@PathVariable Long campId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        int campAllLike = campService.unlikeCamp(campId, userDetails.getUser());
        ApiResponse<String> response = ApiResponse.<String>builder()
            .msg(ResponseText.LIKE_CALL_OFF_SUCCESS.format(campAllLike))
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.ok(response);
    }
}