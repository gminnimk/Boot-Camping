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

    @GetMapping("/api/review")
    public String showReview() { return "review/reviewMain"; }

    @GetMapping("/api/review/detail")
    public String showReviewDetail() { return "review/reviewDetail"; }

    @GetMapping("/api/review/add")
    public String showAddReview() { return "review/reviewAdd"; }
}
