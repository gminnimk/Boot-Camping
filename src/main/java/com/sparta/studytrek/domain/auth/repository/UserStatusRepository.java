package com.sparta.studytrek.domain.auth.repository;

import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.entity.match.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
    List<UserStatus> findByUser(User user);
}
