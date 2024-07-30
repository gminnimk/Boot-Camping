package com.sparta.studytrek.domain.auth.entity;


import com.sparta.studytrek.common.Timestamped;
import com.sparta.studytrek.domain.auth.entity.match.CampUser;
import com.sparta.studytrek.domain.auth.entity.match.UserStatus;
import com.sparta.studytrek.domain.camp.entity.Camp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import com.sparta.studytrek.domain.like.entity.StudyLike;
import com.sparta.studytrek.domain.reply.entity.StudyReply;
import com.sparta.studytrek.domain.study.entity.Study;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false, length = 250)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(name = "user_addr", length = 150)
    private String userAddr;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserType userType;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UserStatus> userStatuses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CampUser> campUsers = new ArrayList<>();

    // 유저와 스터디 연관관계
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Study> studies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyLike> studyLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyReply> studyReplies = new ArrayList<>();

    public User(String username, String password, String name, String userAddr, UserType userType,
        Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.userAddr = userAddr;
        this.userType = userType;
        this.role = role;
    }

    public void addStatus(Status status) {
        UserStatus userStatus = new UserStatus(this, status);
        userStatuses.add(userStatus);
        status.getUserStatuses().add(userStatus);
    }

    public void addStatus(Status status, Camp camp) {
        UserStatus userStatus = new UserStatus(this, status, camp);
        userStatuses.add(userStatus);
        status.getUserStatuses().add(userStatus);
    }

    public void addCamp(Camp camp) {
        CampUser campUser = new CampUser(this, camp);
        campUsers.add(campUser);
        camp.getCampUsers().add(campUser);
    }

    public void addCamp(Camp camp, LocalDate periodStart, LocalDate periodEnd, String trek) {
        CampUser campUser = new CampUser(this, camp, periodStart, periodEnd, trek);
        campUsers.add(campUser);
        camp.getCampUsers().add(campUser);
    }

    public UserStatusEnum getStatus() {
        // userStatuses 리스트가 비어 있지 않다면 최신 상태를 반환
        if (!userStatuses.isEmpty()) {
            return userStatuses.get(userStatuses.size() - 1).getStatus().getStatus();
        }
        // userStatuses 리스트가 비어 있다면 기본 상태를 반환
        return UserStatusEnum.GENERAL;
    }

    // 사용자 상태를 LEAVE(탈퇴) 로 변경
    public void withdraw() {
        this.userType = UserType.LEAVE;
    }

    // 사용자가 탈퇴 상태이면 true or false
    public boolean isWithdrawn() {
        return this.userType == UserType.LEAVE;
    }
}