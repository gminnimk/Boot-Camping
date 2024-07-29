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

    @GetMapping("/api/question")
    public String showQuestion(){
        return "question";
    }

    @GetMapping("/api/profile")
    public String showProfilePage() {
        return "profile";
    }

    @GetMapping("/api/review")
    public String showReview() { return "review/reviewMain"; }

    @GetMapping("/api/review/detail")
    public String showReviewDetail() { return "review/reviewDetail"; }

    @GetMapping("/api/review/add")
    public String showAddReview() { return "review/reviewAdd"; }

    @GetMapping("/api/admin")
    public String showAdmin() {
        return "admin";
    }

    @GetMapping("/api/camp")
    public String showCamp() { return "recruitment/recruitmentMain"; }

    @GetMapping("/api/camp/add")
    public String showAddCamp() { return "recruitment/recruitmentAdd"; }

    @GetMapping("/api/camp/detail")
    public String showCampDetail() { return "recruitment/recruitmentDetail"; }
}
