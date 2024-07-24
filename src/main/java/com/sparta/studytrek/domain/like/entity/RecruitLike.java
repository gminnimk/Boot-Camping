package com.sparta.studytrek.domain.like.entity;

import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.recruitment.entity.Recruitment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recruit_like")
@Getter
@NoArgsConstructor
public class RecruitLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "recruitment_id", nullable = false)
    private Recruitment recruitment;
}
