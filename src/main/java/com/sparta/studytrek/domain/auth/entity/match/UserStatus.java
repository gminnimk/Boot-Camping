package com.sparta.studytrek.domain.auth.entity.match;

import com.sparta.studytrek.domain.auth.entity.Status;
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
@Table(name = "user_status")
@Getter
@NoArgsConstructor
public class UserStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "camp_id")
    private Camp camp;

    public UserStatus(User user, Status status) {
        this.user = user;
        this.status = status;
    }

    public UserStatus(User user, Status status, Camp camp) {
        this.user = user;
        this.status = status;
        this.camp = camp;
    }
}
