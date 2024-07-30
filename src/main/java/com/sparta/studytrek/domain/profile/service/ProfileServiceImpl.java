package com.sparta.studytrek.domain.profile.service;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.service.UserService;
import com.sparta.studytrek.domain.profile.dto.ProfileRequestDto;
import com.sparta.studytrek.domain.profile.dto.ProfileResponseDto;
import com.sparta.studytrek.domain.profile.entity.Profile;
import com.sparta.studytrek.domain.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;
	private final UserService userService;

	@Override
	@Transactional
	public ResponseEntity<ProfileResponseDto> createProfile(String username, ProfileRequestDto requestDto) {
		User user = userService.findByUsername(username);
		Profile profile = new Profile(
			requestDto.getBootcampName(),
			requestDto.getTrack(),
			requestDto.getGeneration(),
			requestDto.getStartDate(),
			requestDto.getEndDate(),
			requestDto.getCertificate(),
			user,
			requestDto.getTechStack()
		);
		profileRepository.save(profile);
		ProfileResponseDto responseDto = new ProfileResponseDto(profile);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	@Override
	public ResponseEntity<List<ProfileResponseDto>> getProfileByUserId(String username) {
		User user = userService.findByUsername(username);
		List<Profile> profiles = profileRepository.findAllByUserId(user.getId());
		if (profiles.isEmpty()) {
			throw new CustomException(ErrorCode.NOTFOUND_PROFILE);
		}
		List<ProfileResponseDto> responseDtos = profiles.stream()
			.map(ProfileResponseDto::new)
			.collect(Collectors.toList());
		return ResponseEntity.ok(responseDtos);
	}

	@Override
	@Transactional
	public ResponseEntity<ProfileResponseDto> updateProfile(Long profileId, ProfileRequestDto requestDto,
		UserDetails userDetails) {
		Profile profile = getProfileAndCheckAuthorization(profileId, userDetails);
		profile.updateProfile(requestDto);
		profileRepository.save(profile);
		ProfileResponseDto responseDto = new ProfileResponseDto(profile);
		return ResponseEntity.ok(responseDto);
	}

	@Override
	@Transactional
	public ResponseEntity<Void> deleteProfile(Long profileId, UserDetails userDetails) {
		Profile profile = getProfileAndCheckAuthorization(profileId, userDetails);
		profileRepository.delete(profile);
		return ResponseEntity.noContent().build();
	}

	private Profile getProfileAndCheckAuthorization(Long profileId, UserDetails userDetails) {
		Profile profile = profileRepository.findById(profileId).orElseThrow(
			() -> new CustomException(ErrorCode.NOTFOUND_PROFILE)
		);
		if (!profile.getUser().getUsername().equals(userDetails.getUsername())) {
			throw new CustomException(ErrorCode.PROFILE_NOT_AUTHORIZED);
		}
		return profile;
	}
}
