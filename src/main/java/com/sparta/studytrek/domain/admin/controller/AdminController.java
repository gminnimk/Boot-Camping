package com.sparta.studytrek.domain.admin.controller;

import com.sparta.studytrek.domain.admin.service.AdminServiceImpl;
import com.sparta.studytrek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminServiceImpl adminService;

    /**
     * 회원탈퇴(관리자용)
     *
     * @param username
     * @param adminDetails
     */
    @PutMapping("/auth/delete")
    public void adminDelete(@RequestParam String username,
                              @AuthenticationPrincipal UserDetailsImpl adminDetails) {
        adminService.adminDelete(username, adminDetails.getUser());
    }
}
