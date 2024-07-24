package com.sparta.studytrek.domain.auth.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.dto.TokenResponseDto;
import com.sparta.studytrek.domain.auth.entity.RefreshToken;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.repository.RefreshTokenRepository;
import com.sparta.studytrek.domain.auth.repository.UserRepository;
import com.sparta.studytrek.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public TokenResponseDto reissueToken(HttpServletRequest request) {
        // Request에서 Refresh Token 추출
        String refreshToken = jwtUtil.extractRefreshToken(request);

        // Refresh Token 유효성 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        // Refresh Token으로 사용자 정보 조회
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        User user = userRepository.findByUsername(storedToken.getUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 새로운 Access Token 및 Refresh Token 생성
        String newAccessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole().toString());
        String newRefreshToken = jwtUtil.createRefreshToken(user.getUsername(), String.valueOf(user.getRole()));

        // Refresh Token 저장소 업데이트
        storedToken.updateToken(newRefreshToken);

        // 새로운 토큰 반환
        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void saveRefreshToken(Long userId, String refreshToken) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );
        RefreshToken token = refreshTokenRepository.findByUserId(userId)
                .orElseGet(() -> new RefreshToken(refreshToken, user));

        token.updateToken(refreshToken);
        refreshTokenRepository.save(token);
    }

    @Transactional
    public void removeRefreshToken(Long userId) {
        RefreshToken token = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NOT_FOUND));

        refreshTokenRepository.delete(token);
    }

}