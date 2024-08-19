package com.sparta.studytrek.domain.like.entity;

import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.camp.entity.Camp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "camp_like")
@Getter
@NoArgsConstructor
public class CampLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "camp_id", nullable = false)
    private Camp camp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public CampLike(Camp camp, User user) {
        this.camp = camp;
        this.user = user;
    }
}