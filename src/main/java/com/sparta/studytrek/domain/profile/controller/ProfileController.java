package com.sparta.studytrek.domain.profile.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.common.ResponseText;
import com.sparta.studytrek.domain.profile.dto.ProfileRequestDto;
import com.sparta.studytrek.domain.profile.dto.ProfileResponseDto;
import com.sparta.studytrek.domain.profile.service.ProfileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 프로필 생성 API
     *
     * @param requestDto  프로필 생성 데이터
     * @param userDetails 인증된 유저 정보
     * @return 프로필 생성 응답 데이터
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createProfile(@RequestBody ProfileRequestDto requestDto,
        @AuthenticationPrincipal UserDetails userDetails)
    {
        ProfileResponseDto responseDto = profileService.createProfile(userDetails.getUsername(),
            requestDto).getBody();
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.PROFILE_CREATE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 로그인한 유저의 모든 프로필 조회 (등록정보) API
     *
     * @param userDetails 인증된 유저 정보
     * @return 유저의 모든 프로필 (등록정보)
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getProfileByUserId(
        @AuthenticationPrincipal UserDetails userDetails)
    {
        List<ProfileResponseDto> responseDto = profileService.getProfileByUserId(
            userDetails.getUsername()).getBody();
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.PROFILE_GET_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 프로필 수정 API
     *
     * @param profileId   프로필 ID
     * @param requestDto  프로필 수정 정보
     * @param userDetails 인증된 유저 정보
     * @return 프로필 수정 응답 데이터
     */
    @PutMapping("/{profileId}")
    public ResponseEntity<ApiResponse> updateProfile(@PathVariable("profileId") Long profileId,
        @RequestBody ProfileRequestDto requestDto,
        @AuthenticationPrincipal UserDetails userDetails)
    {
        ProfileResponseDto responseDto = profileService.updateProfile(profileId, requestDto,
            userDetails).getBody();
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.PROFILE_UPDATE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 프로필 삭제 API
     *
     * @param profileId   프로필 ID
     * @param userDetails 인증된 유저 정보
     * @return 프로필 삭제 응답 데이터
     */
    @DeleteMapping("/{profileId}")
    public ResponseEntity<ApiResponse> deleteProfile(@PathVariable("profileId") Long profileId,
        @AuthenticationPrincipal UserDetails userDetails)
    {
        profileService.deleteProfile(profileId, userDetails);
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.PROFILE_DELETE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.NO_CONTENT.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 프로필 신청 API
     *
     * @param profileId   프로필 ID
     * @param userDetails 인증된 유저 정보
     * @return 프로필 상태 변경 응답 데이터
     */
    @PostMapping("/{profileId}/apply")
    public ResponseEntity<ApiResponse> applyForProfile(@PathVariable("profileId") Long profileId,
        @AuthenticationPrincipal UserDetails userDetails)
    {
        profileService.applyForProfile(profileId, userDetails);
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.PROFILE_APPLY_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.ok().body(response);
    }
}
