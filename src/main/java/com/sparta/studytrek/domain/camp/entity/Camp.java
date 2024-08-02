package com.sparta.studytrek.domain.camp.entity;

import com.sparta.studytrek.domain.auth.entity.match.CampUser;
import com.sparta.studytrek.domain.review.entity.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "camps")
@Getter
@NoArgsConstructor
public class Camp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;

//    @Column(length = 50)
//    private String track; // 필터링을 위한 필드 추가
//
//    @Column(length = 50)
//    private String environment; // 필터링을 위한 필드 추가
//
//    @Column(length = 50)
//    private String cost; // 필터링을 위한 필드 추가

    @OneToMany(mappedBy = "camp", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampUser> campUsers = new ArrayList<>();

//    @OneToMany(mappedBy = "camp", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Rank> ranks = new ArrayList<>();

    @OneToMany(mappedBy = "camp", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public Camp(String name, String description) {
        this.name = name;
        this.description = description;
//        this.track = track;
//        this.environment = environment;
//        this.cost = cost;
    }
}
