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

    @GetMapping("/api/rank")
    public String showRank() {
        return "rank/Rank";
    }
    @GetMapping("/api/study/detail")
    public String showStudyDetail() {
        return "study/StudyDetail";
    }

    @GetMapping("/api/study/main")
    public String showStudyMain() {
        return "study/StudyMain";
    }

    @GetMapping("/api/study/modify")
    public String showStudyModify() {
        return "study/StudyModify";
    }

    @GetMapping("/api/study/write")
    public String showStudyWrite() {
        return "study/StudyWrite";
    }
}
