package com.sparta.studytrek.domain.admin.entity;

import com.sparta.studytrek.common.Timestamped;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Admin extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String adminToken;

    @Column(nullable = false)
    private String status = "ADMIN";

    public Admin(String username, String password, String adminToken) {
        this.username = username;
        this.password = password;
        this.adminToken = adminToken;
        this.status = "ADMIN";
    }
}
