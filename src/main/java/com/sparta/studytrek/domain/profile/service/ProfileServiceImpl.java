package com.sparta.studytrek.domain.profile.service;

import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.service.UserService;
import com.sparta.studytrek.domain.profile.dto.ProfileRequestDto;
import com.sparta.studytrek.domain.profile.entity.Profile;
import com.sparta.studytrek.domain.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;
	private final UserService userService;

	@Override
	@Transactional
	public ResponseEntity<?> createProfile(String username, ProfileRequestDto requestDto) {
		User user = userService.findByUsername(username);
		Profile profile = new Profile(
			requestDto.getBootcampName(),
			requestDto.getTrack(),
			requestDto.getGeneration(),
			requestDto.getStartDate(),
			requestDto.getEndDate(),
			requestDto.getCertificate(),
			user
		);
		profileRepository.save(profile);
		return ResponseEntity.ok("프로필 생성 성공.");
	}

	@Override
	public ResponseEntity<?> getProfileByUserId(String username) {
		User user = userService.findByUsername(username);
		List<Profile> profiles = profileRepository.findAllByUserId(user.getId());
		return ResponseEntity.ok(profiles);
	}

	@Override
	@Transactional
	public ResponseEntity<?> updateProfile(Long profileId, ProfileRequestDto requestDto) {
		Profile profile = profileRepository.findById(profileId).orElseThrow(
			() -> new IllegalArgumentException("프로필을 찾을 수 없습니다.")
		);
		profile.updateProfile(requestDto);
		profileRepository.save(profile);
		return ResponseEntity.ok("프로필 수정 성공.");
	}

	@Override
	@Transactional
	public ResponseEntity<?> deleteProfile(Long profileId) {
		profileRepository.deleteById(profileId);
		return ResponseEntity.ok("프로필 삭제 성공.");
	}
}
