package com.sparta.studytrek.domain.auth.entity.match;

import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.camp.entity.Camp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "user_camps")
@Getter
@NoArgsConstructor
public class UserCamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id", nullable = false)
    private Camp camp;

    @Column(length = 30)
    private String trek;

    @Column(name = "period_start")
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

    public UserCamp(User user, Camp camp) {
        this.user = user;
        this.camp = camp;
    }

    public UserCamp(User user, Camp camp, LocalDate periodStart, LocalDate periodEnd, String trek) {
        this.user = user;
        this.camp = camp;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.trek = trek;
    }
}
