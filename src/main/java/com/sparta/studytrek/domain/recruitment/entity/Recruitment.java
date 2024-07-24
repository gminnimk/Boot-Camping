package com.sparta.studytrek.domain.recruitment.entity;

import com.sparta.studytrek.common.Timestamped;
import jakarta.persistence.*;
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
    private String content;  // 모집글 내용

    @Column(nullable = false, length = 150)
    private String place;  // 캠프 장소

    @Column(nullable = false)
    private int cost;  // 캠프 비용

    @Column(nullable = false, length = 100)
    private String subject;  // 캠프 주제

}
