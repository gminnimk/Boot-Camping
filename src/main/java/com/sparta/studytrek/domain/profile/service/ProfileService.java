package com.sparta.studytrek.domain.profile.service;

import com.sparta.studytrek.domain.profile.dto.ProfileRequestDto;
import org.springframework.http.ResponseEntity;

public interface ProfileService {
	ResponseEntity<?> createProfile(String username, ProfileRequestDto requestDto);
	ResponseEntity<?> getProfileByUserId(String username);
	ResponseEntity<?> updateProfile(Long profileId, ProfileRequestDto requestDto);
	ResponseEntity<?> deleteProfile(Long profileId);
}
