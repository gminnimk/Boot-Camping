package com.sparta.studytrek.domain.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.admin.dto.AdminRequestDto;
import com.sparta.studytrek.domain.admin.dto.AdminResponseDto;
import com.sparta.studytrek.domain.admin.service.AdminService;
import com.sparta.studytrek.domain.auth.dto.TokenResponseDto;
import com.sparta.studytrek.domain.profile.dto.ProfileResponseDto;
import com.sparta.studytrek.domain.profile.entity.ProfileStatus;
import com.sparta.studytrek.domain.profile.service.ProfileService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final ProfileService profileService;

    /**
     * 관리자 회원가입 API
     *
     * @param adminRequestDto 관리자 회원가입 요청 데이터
     * @return 관리자 회원가입 응답 데이터
     */
    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponse> adminSignup(@RequestBody AdminRequestDto adminRequestDto) {
        AdminResponseDto adminResponseDto = adminService.adminSignup(adminRequestDto);
        ApiResponse response = ApiResponse.builder()
            .msg("관리자 회원가입 성공")
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(adminResponseDto)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 관리자 로그인 API
     *
     * @param adminRequestDto 로그인 요청 데이터
     * @return JWT 토큰 응답 데이터
     */
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse> adminLogin(@RequestBody AdminRequestDto adminRequestDto) {
        TokenResponseDto responseDto = adminService.adminLogin(adminRequestDto);
        ApiResponse response = ApiResponse.builder()
            .msg("관리자 로그인 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDto)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 관리자가 일반 유저를 강제로 회원탈퇴 시키는 API
     *
     * @param userId 탈퇴시킬 유저의 ID
     * @return 회원탈퇴 성공 응답 데이터
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> adminDelete(@PathVariable("userId") Long userId) {
        adminService.adminDelete(userId);
        ApiResponse response = ApiResponse.builder()
            .msg("회원 탈퇴 성공")
            .statuscode(String.valueOf(HttpStatus.NO_CONTENT.value()))
            .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    /**
     * 프로필 승인 API (관리자용)
     *
     * @param profileId 승인할 프로필의 ID
     * @return 승인 성공 응답 데이터
     */
    @PostMapping("/profiles/{profileId}/approve")
    public ResponseEntity<ApiResponse> approveProfile(@PathVariable("profileId") Long profileId) {
        profileService.approveProfile(profileId);
        ApiResponse response = ApiResponse.builder()
            .msg("프로필 승인 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 프로필 거절 API (관리자용)
     *
     * @param profileId 거절할 프로필의 ID
     * @return 거절 성공 응답 데이터
     */
    @PostMapping("/profiles/{profileId}/reject")
    public ResponseEntity<ApiResponse> rejectProfile(@PathVariable("profileId") Long profileId) {
        profileService.rejectProfile(profileId);
        ApiResponse response = ApiResponse.builder()
            .msg("프로필 거절 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 모든 유저의 프로필 전체 조회 (관리자용)
     *
     * @return 조회 성공 응답 데이터
     */
    @GetMapping("/profiles")
    public ResponseEntity<ApiResponse> getAllProfiles() {
        List<ProfileResponseDto> responseDtos = profileService.getAllProfiles().getBody();
        ApiResponse response = ApiResponse.builder()
            .msg("전체 프로필 조회 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDtos)
            .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 프로필 상태별 조회 (관리자용)
     *
     * @param status 조회할 프로필 상태를 선택
     * @return 조회 성공 응답 데이터
     */
    @GetMapping("/profiles/status/{status}")
    public ResponseEntity<ApiResponse> getProfileByStatus(@PathVariable ProfileStatus status) {
        List<ProfileResponseDto> responseDtos = profileService.getProfilesByStatus(status).getBody();
        ApiResponse response = ApiResponse.builder()
            .msg(status + " 상태의 프로필 조회 성공")
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(responseDtos)
            .build();
        return ResponseEntity.ok(response);
    }
}
