package com.sparta.studytrek.domain.auth.entity;

import com.sparta.studytrek.domain.auth.entity.match.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "status")
@Getter
@NoArgsConstructor
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserStatusEnum status;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStatus> userStatuses = new ArrayList<>();

    public Status(UserStatusEnum status) {
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            this.status = UserStatusEnum.getDefault();
        }
    }
}
