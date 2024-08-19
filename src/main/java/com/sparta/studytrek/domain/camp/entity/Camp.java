package com.sparta.studytrek.domain.camp.entity;

import com.sparta.studytrek.domain.auth.entity.match.CampUser;
import com.sparta.studytrek.domain.rank.entity.Rank;
import com.sparta.studytrek.domain.recruitment.entity.Recruitment;
import com.sparta.studytrek.domain.review.entity.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    private String summary = "";

    @Getter
    @Column(length = 255) // 이미지 URL을 저장하기 위한 필드
    private String imageUrl;

    @Getter
    @Column(nullable = false)
    private int likesCount = 0;
    
    public void incrementLikes() {
        this.likesCount++;
    }

    public void decrementLikes() {
        if (this.likesCount > 0) {
            this.likesCount--;
        }
    }

    @OneToMany(mappedBy = "camp", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampUser> campUsers = new ArrayList<>();

    @OneToMany(mappedBy = "camp", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rank> ranks = new ArrayList<>();

    @OneToMany(mappedBy = "camp", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    public Camp(String name, String description, String summary, Recruitment recruitment) {
        this.name = name;
        this.description = description;
        this.summary = summary;
        this.recruitment = recruitment;
    }

    public Camp(String name, String description, String imageUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public void updateSummary(String summary) {
        this.summary = summary;
    }
}