package com.sparta.studytrek.domain.recruitment.dto;

import com.sparta.studytrek.domain.recruitment.entity.Recruitment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class RecruitmentResponseDto {
    private Long id;
    private String title;
    private String process;
    private String content;
    private String place;
    private String cost;
    private String trek;
    private String level;
    private String classTime;
    private LocalDate campStart;
    private LocalDate campEnd;
    private LocalDate recruitStart;
    private LocalDate recruitEnd;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String campName;
    private String imageUrl;

    public RecruitmentResponseDto(Recruitment createRecruitment) {
        this.id = createRecruitment.getId();
        this.title = createRecruitment.getTitle();
        this.process = createRecruitment.getProcess();
        this.content = createRecruitment.getContent();
        this.place = createRecruitment.getPlace();
        this.cost = createRecruitment.getCost();
        this.trek = createRecruitment.getTrek();
        this.level = createRecruitment.getLevel();
        this.classTime = createRecruitment.getClassTime();
        this.campStart = createRecruitment.getCampStart();
        this.campEnd = createRecruitment.getCampEnd();
        this.recruitStart = createRecruitment.getRecruitStart();
        this.recruitEnd = createRecruitment.getRecruitEnd();
        this.createAt = createRecruitment.getCreatedAt();
        this.updateAt = createRecruitment.getUpdatedAt();
        this.campName = createRecruitment.getCampName();
        this.imageUrl = createRecruitment.getImageUrl();
    }
}
