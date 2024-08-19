package com.sparta.studytrek.domain.profile.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.studytrek.common.ResponseText;
import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.Role;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.entity.UserRoleEnum;
import com.sparta.studytrek.domain.auth.service.UserService;
import com.sparta.studytrek.domain.profile.dto.ProfileRequestDto;
import com.sparta.studytrek.domain.profile.dto.ProfileResponseDto;
import com.sparta.studytrek.domain.profile.entity.Profile;
import com.sparta.studytrek.domain.profile.entity.ProfileStatus;
import com.sparta.studytrek.domain.profile.repository.ProfileRepository;
import com.sparta.studytrek.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;
	private final UserService userService;
	private final NotificationService notificationService;

	@Override
	@Transactional
	public ProfileResponseDto createProfile(String username, ProfileRequestDto requestDto) {
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
		return new ProfileResponseDto(profile);
	}

	@Override
	public List<ProfileResponseDto> getProfileByUserId(String username) {
		User user = userService.findByUsername(username);
		List<Profile> profiles = profileRepository.findAllByUserId(user.getId());
		if (profiles.isEmpty()) {
			throw new CustomException(ErrorCode.NOTFOUND_PROFILE);
		}
		return profiles.stream()
			.map(ProfileResponseDto::new)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ProfileResponseDto updateProfile(Long profileId, ProfileRequestDto requestDto,
		UserDetails userDetails) {
		Profile profile = getProfileAndCheckAuthorization(profileId, userDetails);
		profile.updateProfile(requestDto);
		profileRepository.save(profile);
		return new ProfileResponseDto(profile);
	}

	@Override
	@Transactional
	public void deleteProfile(Long profileId, UserDetails userDetails) {
		Profile profile = getProfileAndCheckAuthorization(profileId, userDetails);
		profileRepository.delete(profile);
	}


	@Override
	@Transactional
	public void approveProfile(Long profileId) throws IOException{
		Profile profile = findProfileById(profileId);
		profile.approveProfile();
		profileRepository.save(profile);

		notificationService.createAndSendNotification(
			profile.getUser().getUsername(),
			ResponseText.NOTIFICATION_PROFILE_APPROVED.getMsg()
		);
	}

	@Override
	@Transactional
	public void rejectProfile(Long profileId) throws IOException {
		Profile profile = findProfileById(profileId);
		profile.rejectProfile();
		profileRepository.save(profile);

		notificationService.createAndSendNotification(
			profile.getUser().getUsername(),
			ResponseText.NOTIFICATION_PROFILE_REJECTED.getMsg()
		);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProfileResponseDto> getProfilesByRole(UserRoleEnum roleEnum) {
		Role role = userService.findRoleByName(roleEnum);

		List<Profile> profiles = profileRepository.findAllByUserRoleAndStatusNot(role, ProfileStatus.BASIC);
		return profiles.stream()
			.map(ProfileResponseDto::new)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProfileResponseDto> getProfilesByRoleAndStatus(UserRoleEnum roleEnum, ProfileStatus status) {
		Role role = userService.findRoleByName(roleEnum);

		List<Profile> profiles = profileRepository.findAllByUserRoleAndStatus(role, status);
		return profiles.stream()
			.map(ProfileResponseDto::new)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ProfileResponseDto getProfileById(Long profileId) {
		Profile profile = findProfileById(profileId);
		return new ProfileResponseDto(profile);
	}

	@Override
	@Transactional
	public void applyForProfile(Long profileId, UserDetails userDetails) {
		Profile profile = getProfileAndCheckAuthorization(profileId, userDetails);
		profile.apply();
		profileRepository.save(profile);
	}

	private Profile getProfileAndCheckAuthorization(Long profileId, UserDetails userDetails) {
		Profile profile = findProfileById(profileId);
		if (!profile.getUser().getUsername().equals(userDetails.getUsername())) {
			throw new CustomException(ErrorCode.PROFILE_NOT_AUTHORIZED);
		}
		return profile;
	}

	private Profile findProfileById(Long profileId) {
		return profileRepository.findById(profileId).orElseThrow(
			() -> new CustomException(ErrorCode.NOTFOUND_PROFILE)
		);
	}
}
