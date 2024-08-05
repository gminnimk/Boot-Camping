package com.sparta.studytrek.domain.profile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.studytrek.domain.auth.entity.Role;
import com.sparta.studytrek.domain.profile.entity.Profile;
import com.sparta.studytrek.domain.profile.entity.ProfileStatus;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
	List<Profile> findAllByUserId(Long userId);
	List<Profile> findAllByUserRoleAndStatusNot(Role role, ProfileStatus status);
	List<Profile> findAllByUserRoleAndStatus(Role role, ProfileStatus status);
}
