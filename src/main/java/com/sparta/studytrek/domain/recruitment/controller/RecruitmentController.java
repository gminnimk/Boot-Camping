package com.sparta.studytrek.domain.recruitment.controller;

import com.sparta.studytrek.domain.recruitment.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/camps")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;



}
