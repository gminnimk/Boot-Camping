package com.sparta.studytrek.domain.auth.repository;

import com.sparta.studytrek.domain.auth.entity.Status;
import com.sparta.studytrek.domain.auth.entity.UserStatusEnum;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    // 상태 이름으로 Status 객체를 찾는 메서드
    Optional<Status> findByStatus(UserStatusEnum status);
}
