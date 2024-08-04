package com.sparta.studytrek.domain.admin.service;

import com.sparta.studytrek.domain.admin.dto.AdminRequestDto;
import com.sparta.studytrek.domain.admin.dto.AdminResponseDto;
import com.sparta.studytrek.domain.auth.dto.TokenResponseDto;

import jakarta.transaction.Transactional;

public interface AdminService {
	AdminResponseDto adminSignup(AdminRequestDto adminRequestDto);
	TokenResponseDto adminLogin(AdminRequestDto adminRequestDto);
	void adminDelete(Long userId);
}
