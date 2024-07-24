package com.sparta.studytrek.domain.admin.controller;

import com.sparta.studytrek.domain.admin.service.AdminService;
import com.sparta.studytrek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 회원탈퇴(관리자용)
     *
     * @param username
     * @param adminDetails
     */
    @PutMapping("/auth/withdraw")
    public void adminWithdraw(@RequestParam String username,
                              @AuthenticationPrincipal UserDetailsImpl adminDetails) {
        adminService.adminWithdraw(username, adminDetails.getUser());
    }

}
