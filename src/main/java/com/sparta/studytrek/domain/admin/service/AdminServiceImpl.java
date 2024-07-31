package com.sparta.studytrek.domain.admin.service;

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

    /**
     *  관리자 회원가입
     *
     * @param adminRequestDto
     * @return 저장된 User 를 AdminResponseDto로 변환하여 반환함.
     */
    @Override
    @Transactional
    public AdminResponseDto adminSignup(AdminRequestDto adminRequestDto) {
        String encodedPassword = passwordEncoder.encode(adminRequestDto.password());

        User user = adminMapper.toUser(adminRequestDto);

        Role adminRole = userService.findRoleByName(UserRoleEnum.ADMIN);
        user = new User(
            user.getUsername(),
            encodedPassword,
            user.getName(),
            user.getUserAddr(),
            user.getUserType(),
            adminRole
        );

        if (userService.findByUsername(user.getUsername()) != null) {
            throw new CustomException(ErrorCode.DUPLICATE_USER);
        }

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

    /**
     * 사용자 상태를 변경하는 일반화된 메소드
     *
     * @param username  상태 변경할 사용자 이름
     * @param newStatus 설정할 새로운 상태
     * @param campId    관련 캠프의 Id
     * @param adminUser 관리자 정보(권한 체크)
     */
    // @Override
    // @Transactional
    // public ResponseEntity<Void> changeUserStatus(String username, UserStatusEnum newStatus, Long campId,
    //     User adminUser) {
    //     validateAdminAuthority(adminUser);
    //
    //     User user = userService.findByUsername(username);
    //
    //     // 해당 사용자의 현재 상태를 조회 (campId 를 사용하여 특정 캠프와 관련된 상태 get)
    //     UserStatus userStatus = userStatusService.getUserStatus(username, campId);
    //     if (userStatus == null) {
    //         throw new CustomException(ErrorCode.USER_STATUS_NOT_FOUND);
    //     }
    //     UserStatus status = userStatusService.findByStatus(newStatus);
    //     if (status == null) {
    //         throw new CustomException(ErrorCode.STATUS_NOT_FOUND);
    //     }
    //     // 기존 상태를 제거하고 새로운 상태를 추가
    //     user.getUserStatuses().clear();
    //     user.addStatus(status.getStatus());
    //     userService.saveUser(user);
    //
    //     return ResponseEntity.ok().build();
    // }



    // /**
    //  * 관리자가 사용자 상태를 APPROVER로 변경하는 메소드
    //  *
    //  * @param username
    //  * @param adminDetails
    //  */
    // @Override
    // @Transactional
    // public ResponseEntity<Void> approveUser(String username, UserDetailsImpl adminDetails) {
    //     changeUserStatus(username, UserStatusEnum.APPROVER, null, adminDetails.getUser());
    //     return ResponseEntity.ok().build();
    // }
}
