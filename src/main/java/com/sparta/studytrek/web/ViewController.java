package com.sparta.studytrek.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/api/auth")
    public String showAuthPage() {
        return "login"; // 로그인&회원가입
    }

    @GetMapping("/api/home")
    public String showHome() {
        return "home";
    }

    @GetMapping("/api/camp")
    public String showCamp() { return "recruitment/recruitmentMain"; }

    @GetMapping("/api/camp/add")
    public String showAddCamp() { return "recruitment/recruitmentAdd"; }

    @GetMapping("/api/camp/detail")
    public String showCampDetail() { return "recruitment/recruitmentDetail"; }
}
