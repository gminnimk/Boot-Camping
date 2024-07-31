package com.sparta.studytrek.domain.admin.repository;

import java.util.Optional;

import com.sparta.studytrek.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
	// 유저이름으로 관리자 찾기
	Optional<Admin> findByUsername(String username);
	// adminToken이 이미 존재하는지 확인
	boolean existsByAdminToken(String adminToken);
	// 유저이름이 이미 존재하는지 확인
	boolean existsByUsername(String username);
}
