package com.sparta.studytrek.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/auth")
    public String showAuthPage() {
        return "login"; // 로그인&회원가입
    }

    @GetMapping("/home")
    public String showHome() {
        return "home";
    }

    @GetMapping("/question")
    public String showQuestion(){
        return "question";
    }

    @GetMapping("/admin")
    public String showAdmin() {
        return "admin";
    }

    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile";
    }

    @GetMapping("/review")
    public String showReview() { return "review/reviewMain"; }

    @GetMapping("/review/{reviewId}")
    public String showReviewDetail() { return "review/reviewDetail"; }

    @GetMapping("/review/add")
    public String showAddReview() { return "review/reviewAdd"; }

    @GetMapping("/rank")
    public String showRank() {
        return "rank/Rank";
    }
    @GetMapping("/study/detail")
    public String showStudyDetail() {
        return "study/StudyDetail";
    }

    @GetMapping("/study/main")
    public String showStudyMain() {
        return "study/StudyMain";
    }

    @GetMapping("/study/modify")
    public String showStudyModify() {
        return "study/StudyModify";
    }

    @GetMapping("/study/write")
    public String showStudyWrite() {
        return "study/StudyWrite";
    }

    @GetMapping("/study/{id}")
    public String showStudyDetailId() {
        return "study/StudyDetail";
    }

    @GetMapping("/camp")
    public String showCamp() { return "recruitment/recruitmentMain"; }

    @GetMapping("/camp/add")
    public String showAddCamp() { return "recruitment/recruitmentAdd"; }

    @GetMapping("/camp/{campId}")
    public String showCampDetail() { return "recruitment/recruitmentDetail"; }

}
