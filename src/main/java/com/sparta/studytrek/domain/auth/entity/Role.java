package com.sparta.studytrek.domain.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "roles")
@Getter
@RequiredArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public Role(UserRoleEnum role) {
        this.role = role;
    }

    @PrePersist
    protected void onCreate() {
        if (this.role == null) {
            this.role = UserRoleEnum.getDefault();
        }
    }
}
