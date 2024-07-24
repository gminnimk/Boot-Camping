package com.sparta.studytrek.domain.camp.entity;

import com.sparta.studytrek.domain.auth.entity.match.UserCamp;
import com.sparta.studytrek.domain.rank.entity.Rank;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "camp", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCamp> userCamps = new ArrayList<>();

    @OneToMany(mappedBy = "camp", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rank> ranks = new ArrayList<>();

    public Camp(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
