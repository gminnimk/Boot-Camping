package com.sparta.studytrek.domain.admin.service;

import org.springframework.http.ResponseEntity;

import com.sparta.studytrek.domain.admin.dto.AdminRequestDto;
import com.sparta.studytrek.domain.admin.dto.AdminResponseDto;
import com.sparta.studytrek.domain.auth.dto.TokenResponseDto;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.entity.UserStatusEnum;
import com.sparta.studytrek.security.UserDetailsImpl;

import jakarta.transaction.Transactional;

public interface AdminService {
	ResponseEntity<AdminResponseDto> adminSignup(AdminRequestDto requestDto);

	ResponseEntity<TokenResponseDto> adminLogin(AdminRequestDto requestDto);

	ResponseEntity<Void> changeUserStatus(String username, UserStatusEnum newStatus, Long campId,
		User adminUser);

	ResponseEntity<Void> adminDelete(String username, User adminUser);

	ResponseEntity<Void> approveUser(String username, UserDetailsImpl adminDetails);
}
