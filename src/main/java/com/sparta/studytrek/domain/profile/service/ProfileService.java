package com.sparta.studytrek.domain.profile.service;

import com.sparta.studytrek.domain.auth.entity.UserRoleEnum;
import com.sparta.studytrek.domain.profile.dto.ProfileRequestDto;
import com.sparta.studytrek.domain.profile.dto.ProfileResponseDto;
import com.sparta.studytrek.domain.profile.entity.ProfileStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProfileService {

	ProfileResponseDto createProfile(String username, ProfileRequestDto requestDto);

	List<ProfileResponseDto> getProfileByUserId(String username);

	ProfileResponseDto updateProfile(Long profileId, ProfileRequestDto requestDto,
		UserDetails userDetails);

	void deleteProfile(Long profileId, UserDetails userDetails);

	void approveProfile(Long profileId);

	void rejectProfile(Long profileId);

	List<ProfileResponseDto> getProfilesByRole(UserRoleEnum roleEnum);

	List<ProfileResponseDto> getProfilesByRoleAndStatus(UserRoleEnum roleEnum, ProfileStatus status);

	ProfileResponseDto getProfileById(Long profileId);

	void applyForProfile(Long profileId, UserDetails userDetails);
}
