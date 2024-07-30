package com.sparta.studytrek.domain.recruitment.entity;

import com.sparta.studytrek.common.Timestamped;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recruitment")
@Getter
@NoArgsConstructor
public class Recruitment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;  // 모집글 제목

    @Column(nullable = false)
    private String process; // 과정 소개

    @Column(nullable = false)
    private String content;  // 모집글 내용

    @Column(nullable = false, length = 150)
    private String place;  // 캠프 장소

    @Column(nullable = false)
    private String cost;  // 캠프 비용

    @Column(nullable = false, length = 100)
    private String subject;  // 캠프 주제

    @Column(nullable = false)
    private String level; // 난이도

    @Column(nullable = false)
    private String classTime;  // 수업시간

    @Column(nullable = false)
    private LocalDate campStart;  // 캠프 시작일

    @Column(nullable = false)
    private LocalDate campEnd;  // 캠프 종료일

    @Column(nullable = false)
    private LocalDate recruitStart; // 모집 시작일

    @Column(nullable = false)
    private LocalDate recruitEnd;  // 모집 종료일
}
