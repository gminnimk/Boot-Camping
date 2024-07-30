package com.sparta.studytrek.domain.profile.service;

import com.sparta.studytrek.domain.profile.dto.ProfileRequestDto;
import com.sparta.studytrek.domain.profile.dto.ProfileResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ProfileService {
	ResponseEntity<ProfileResponseDto> createProfile(String username, ProfileRequestDto requestDto);
	ResponseEntity<List<ProfileResponseDto>> getProfileByUserId(String username);
	ResponseEntity<ProfileResponseDto> updateProfile(Long profileId, ProfileRequestDto requestDto, UserDetails userDetails);
	ResponseEntity<Void> deleteProfile(Long profileId, UserDetails userDetails);
}
