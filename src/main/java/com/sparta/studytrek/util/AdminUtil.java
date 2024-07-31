package com.sparta.studytrek.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.jwt.JwtUtil;


@Component
public class AdminUtil {

	@Value("${admin.token}")
	private String adminToken;

	private final JwtUtil jwtUtil;

	public AdminUtil(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	// Admin Token 검증 메서드
	public void validateAdminToken(String token) {
		if (!adminToken.equals(token)) {
			throw new CustomException(ErrorCode.INVALID_ADMIN_TOKEN);
		}
	}

	// Access Token 생성
	public String createAccessToken(String username, String role) {
		return jwtUtil.createAccessToken(username, role);
	}

	// Refresh Token 생성
	public String createRefreshToken(String username, String role) {
		return jwtUtil.createRefreshToken(username, role);
	}
}
