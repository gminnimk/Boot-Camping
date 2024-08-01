package com.sparta.studytrek.domain.admin.controller;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.domain.admin.dto.AdminRequestDto;
import com.sparta.studytrek.domain.admin.dto.AdminResponseDto;
import com.sparta.studytrek.domain.admin.service.AdminServiceImpl;
import com.sparta.studytrek.domain.auth.dto.TokenResponseDto;
import com.sparta.studytrek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminServiceImpl adminService;

    /**
     * 관리자 회원가입 API
     *
     * @param adminRequestDto 관리자 회원가입 요청 데이터
     * @return 관리자 회원가입 응답 데이터
     */
    @PostMapping("/signup")
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
    @PostMapping("/login")
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
}
