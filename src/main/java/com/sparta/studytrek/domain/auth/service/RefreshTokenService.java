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

    /**
     * 토큰 재발급
     *
     * @param request
     * @return
     */
    @Transactional
    public TokenResponseDto reissueToken(HttpServletRequest request) {
        String refreshToken = jwtUtil.extractRefreshToken(request);

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        User user = userRepository.findByUsername(storedToken.getUser().getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String newAccessToken = jwtUtil.createAccessToken(user.getUsername(),
            user.getRole().toString());
        String newRefreshToken = jwtUtil.createRefreshToken(user.getUsername(),
            String.valueOf(user.getRole()));

        storedToken.updateToken(newRefreshToken);

        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }

    /**
     * 토큰 저장
     *
     * @param userId       사용자 ID
     * @param refreshToken 리프레시 토큰
     */
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

    /**
     * 토큰 삭제
     *
     * @param userId 사용자 ID
     */
    @Transactional
    public void removeRefreshToken(Long userId) {
        RefreshToken token = refreshTokenRepository.findByUserId(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NOT_FOUND));

        refreshTokenRepository.delete(token);
    }
}