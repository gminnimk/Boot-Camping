package com.sparta.studytrek.domain.admin.service;

import com.sparta.studytrek.domain.admin.repository.AdminRepository;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.entity.UserRoleEnum;
import com.sparta.studytrek.domain.auth.entity.UserStatusEnum;
import com.sparta.studytrek.domain.auth.entity.match.UserStatus;
import com.sparta.studytrek.domain.auth.service.RefreshTokenService;
import com.sparta.studytrek.domain.auth.service.UserService;
import com.sparta.studytrek.domain.auth.service.UserStatusService;
import com.sparta.studytrek.domain.camp.service.CampService;
import com.sparta.studytrek.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserService userService;
    private final UserStatusService userStatusService;
    private final CampService campService;
    private final RefreshTokenService refreshTokenService;
    private final AdminRepository adminRepository;



    // 사용자 상태를 변경하는 일반화된 메소드
    // 상태 변경할 사용자 이름, 설정할 새로운 상태, 관련 캠프의 Id, 관리자 정보(권한 체크)
    @Transactional
    public void changeUserStatus(String username, UserStatusEnum newStatus, Long campId, User adminUser) {

        // 권한 체크 로직 추가 (예: 현재 사용자가 관리자 권한을 가지고 있는지 확인)
        if (!adminUser.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new SecurityException("관리자 권한이 필요합니다.");
        }

        User user = userService.findByUsername(username);

        // 해당 사용자의 현재 상태를 조회 (campId 를 사용하여 특정 캠프와 관련된 상태 get)
        UserStatus userStatus = userStatusService.getUserStatus(username, campId);
        if (userStatus == null) {
            throw new IllegalArgumentException("사용자 상태를 찾을 수 없습니다.");
        }
        UserStatus status = userStatusService.findByStatus(newStatus);
        if (status == null) {
            throw new IllegalArgumentException("상태를 찾을 수 없습니다");
        }

        // 상태 update or 생성
//        if (userStatus != null) {
//            // 현재 상태가 존재 => 새로운 // 상태로 업뎃
//            userStatus.setStatus(newStatus);
//        }
//        else {
//            // 현재 상태가 존재 X => 새로운 UserStatus 객체 생성
//            UserStatus newUserStatus = new UserStatus();
//            newUserStatus.setStatus(newStatus);
//            newUserStatus.setUser(user);
//            if (campId != null) { // 캠프 아이디가 있을 시 연결
//                Camp camp = campService.findById(campId);
//                newUserStatus.setCamp(camp);
//            }
//            user.addStatus(newUserStatus);
//        }
        // 기존 상태를 제거하고 새로운 상태를 추가

        user.getUserStatuses().clear();
        user.addStatus(status.getStatus());
        userService.saveUser(user);
    }

    // 관리자가 사용자를 강제 탈퇴시키는 메서드
    @Transactional
    public void adminWithdraw(String username, User adminUser) {
        UserRoleEnum roleEnum = adminUser.getRole().getRole();

        if (!roleEnum.equals(UserRoleEnum.ADMIN)) {
            System.out.println("Admin User Role: " + adminUser.getRole());
            throw new SecurityException("관리자 권한이 필요합니다.");
        }

        User user = userService.findByUsername(username);

        // 사용자 상태(UserType) 를 LEAVE 로 설정
        user.withdraw();
        userService.saveUser(user);
    }

    // 관리자가 사용자 상태를 APPROVER로 변경하는 메소드
    @Transactional
    public void approveUser(String username, UserDetailsImpl adminDetails) {
        changeUserStatus(username, UserStatusEnum.APPROVER,null, adminDetails.getUser());
    }
}
