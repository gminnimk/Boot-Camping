package com.sparta.studytrek.domain.auth.entity.match;

import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.camp.entity.Camp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "camp_users")
@Getter
@NoArgsConstructor
public class CampUser {

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

    public CampUser(User user, Camp camp) {
        this.user = user;
        this.camp = camp;
    }

    public CampUser(User user, Camp camp, LocalDate periodStart, LocalDate periodEnd, String trek) {
        this.user = user;
        this.camp = camp;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.trek = trek;
    }
}
