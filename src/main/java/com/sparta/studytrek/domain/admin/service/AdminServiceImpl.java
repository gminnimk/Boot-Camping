package com.sparta.studytrek.domain.admin.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.admin.dto.AdminRequestDto;
import com.sparta.studytrek.domain.admin.dto.AdminResponseDto;
import com.sparta.studytrek.domain.admin.mapper.AdminMapper;
import com.sparta.studytrek.domain.auth.dto.TokenResponseDto;
import com.sparta.studytrek.domain.auth.entity.Role;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.entity.UserRoleEnum;
import com.sparta.studytrek.domain.auth.entity.UserStatusEnum;
import com.sparta.studytrek.domain.auth.entity.UserType;
import com.sparta.studytrek.domain.auth.entity.match.UserStatus;
import com.sparta.studytrek.domain.auth.repository.UserRepository;
import com.sparta.studytrek.domain.auth.service.RefreshTokenService;
import com.sparta.studytrek.domain.auth.service.UserService;
import com.sparta.studytrek.domain.auth.service.UserStatusService;
import com.sparta.studytrek.jwt.JwtUtil;
import com.sparta.studytrek.security.UserDetailsImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserStatusService userStatusService;
    private final RefreshTokenService refreshTokenService;
    private final AdminMapper adminMapper;
    private final UserRepository userRepository;

    /**
     *  관리자 회원가입
     *
     * @param adminRequestDto
     * @return 저장된 User 를 AdminResponseDto로 변환하여 반환함.
     */
    @Override
    @Transactional
    public AdminResponseDto adminSignup(AdminRequestDto adminRequestDto) {
        Optional<User> existingUser = userRepository.findByUsername(adminRequestDto.username());

        if (existingUser.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USER);
        }

        String encodedPassword = passwordEncoder.encode(adminRequestDto.password());

        Role adminRole = userService.findRoleByName(UserRoleEnum.ADMIN);

        User user = new User(
            adminRequestDto.username(),
            encodedPassword,
            adminRequestDto.name(),
            null,
            UserType.NORMAL,
            adminRole
        );

        userService.saveUser(user);
        return adminMapper.toAdminResponseDto(user);
    }

    /**
     * 관리자 로그인
     *
     * @param adminRequestDto 로그인 요청 데이터
     * @return JWT 토큰을 포함한 로그인 응답 데이터
     */
    @Override
    @Transactional
    public TokenResponseDto adminLogin(AdminRequestDto adminRequestDto) {
        User user = userService.findByUsername(adminRequestDto.username());
        if (user == null || user.isWithdrawn()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(adminRequestDto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
        if (!user.getRole().getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHENTICATED);
        }
        String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole().getRole().toString());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername(), user.getRole().getRole().toString());

        refreshTokenService.saveRefreshToken(user.getId(), refreshToken);
        return new TokenResponseDto(accessToken, refreshToken);
    }

    /**
     * 관리자가 일반 유저를 강제로 회원탈퇴 시킴
     *
     * @param userId 탈퇴시킬 유저의 ID
     */
    @Override
    @Transactional
    public void adminDelete(Long userId) {
        User user = userService.findById(userId);

        if (user.isWithdrawn()) {
            throw new CustomException(ErrorCode.WITHDRAW_USER);
        }
        user.withdraw();
        userService.saveUser(user);
    }
}
