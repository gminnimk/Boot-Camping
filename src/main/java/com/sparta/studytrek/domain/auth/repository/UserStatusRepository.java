package com.sparta.studytrek.domain.auth.repository;

import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.entity.UserStatusEnum;
import com.sparta.studytrek.domain.auth.entity.match.UserStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
    List<UserStatus> findByUser(User user);
    Optional<UserStatus> findByStatus(UserStatusEnum status);
}
