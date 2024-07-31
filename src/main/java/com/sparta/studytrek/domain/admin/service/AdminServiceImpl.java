package com.sparta.studytrek.domain.admin.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.admin.dto.AdminRequestDto;
import com.sparta.studytrek.domain.admin.dto.AdminResponseDto;
import com.sparta.studytrek.domain.admin.entity.Admin;
import com.sparta.studytrek.domain.admin.mapper.AdminMapper;
import com.sparta.studytrek.domain.admin.repository.AdminRepository;
import com.sparta.studytrek.domain.auth.dto.TokenResponseDto;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.entity.UserRoleEnum;
import com.sparta.studytrek.domain.auth.entity.UserStatusEnum;
import com.sparta.studytrek.domain.auth.entity.match.UserStatus;
import com.sparta.studytrek.domain.auth.service.RefreshTokenService;
import com.sparta.studytrek.domain.auth.service.UserService;
import com.sparta.studytrek.domain.auth.service.UserStatusService;
import com.sparta.studytrek.jwt.JwtUtil;
import com.sparta.studytrek.security.UserDetailsImpl;
import com.sparta.studytrek.util.AdminUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final AdminUtil adminUtil;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserStatusService userStatusService;
    private final RefreshTokenService refreshTokenService;


    @Override
    @Transactional
    public ResponseEntity<AdminResponseDto> adminSignup(AdminRequestDto requestDto) {
        adminUtil.validateAdminToken(requestDto.adminToken());

        if (adminRepository.existsByUsername(requestDto.username())) {
            throw new CustomException(ErrorCode.DUPLICATE_USER);
        }

        Admin admin = new Admin(requestDto.username(),passwordEncoder.encode(requestDto.password()),
            requestDto.adminToken());
        adminRepository.save(admin);
        AdminResponseDto responseDto = adminMapper.toAdminResponse(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @Override
    @Transactional
    public ResponseEntity<TokenResponseDto> adminLogin(AdminRequestDto requestDto) {
        Admin admin = adminRepository.findByUsername(requestDto.username()).orElseThrow(
            () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );
        if (!passwordEncoder.matches(requestDto.password(), admin.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        adminUtil.validateAdminToken(requestDto.adminToken());

        String accessToken = jwtUtil.createAccessToken(admin.getUsername(), UserRoleEnum.ADMIN.getUserRole());
        String refreshToken = jwtUtil.createRefreshToken(admin.getUsername(), UserRoleEnum.ADMIN.getUserRole());

        refreshTokenService.saveRefreshToken(admin.getId(), refreshToken);

        TokenResponseDto responseDto = new TokenResponseDto(accessToken, refreshToken);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 사용자 상태를 변경하는 일반화된 메소드
     *
     * @param username  상태 변경할 사용자 이름
     * @param newStatus 설정할 새로운 상태
     * @param campId    관련 캠프의 Id
     * @param adminUser 관리자 정보(권한 체크)
     */
    @Override
    @Transactional
    public ResponseEntity<Void> changeUserStatus(String username, UserStatusEnum newStatus, Long campId,
        User adminUser) {
        validateAdminAuthority(adminUser);

        User user = userService.findByUsername(username);

        // 해당 사용자의 현재 상태를 조회 (campId 를 사용하여 특정 캠프와 관련된 상태 get)
        UserStatus userStatus = userStatusService.getUserStatus(username, campId);
        if (userStatus == null) {
            throw new CustomException(ErrorCode.USER_STATUS_NOT_FOUND);
        }
        UserStatus status = userStatusService.findByStatus(newStatus);
        if (status == null) {
            throw new CustomException(ErrorCode.STATUS_NOT_FOUND);
        }
        // 기존 상태를 제거하고 새로운 상태를 추가
        user.getUserStatuses().clear();
        user.addStatus(status.getStatus());
        userService.saveUser(user);

        return ResponseEntity.ok().build();
    }

    /**
     *  관리자가 사용자를 강제 탈퇴시키는 메서드
     *
     * @param username
     * @param adminUser
     */
    @Override
    @Transactional
    public ResponseEntity<Void> adminDelete(String username, User adminUser) {
        validateAdminAuthority(adminUser);

        User user = userService.findByUsername(username);
        user.withdraw();
        userService.saveUser(user);

        return ResponseEntity.ok().build();
    }

    /**
     * 관리자가 사용자 상태를 APPROVER로 변경하는 메소드
     *
     * @param username
     * @param adminDetails
     */
    @Override
    @Transactional
    public ResponseEntity<Void> approveUser(String username, UserDetailsImpl adminDetails) {
        changeUserStatus(username, UserStatusEnum.APPROVER, null, adminDetails.getUser());
        return ResponseEntity.ok().build();
    }

    // 관리자 권한 확인
    private void validateAdminAuthority(User adminUser) {
        if (!adminUser.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.ADMIN_NOT_AUTHORIZED);
        }
    }
}
