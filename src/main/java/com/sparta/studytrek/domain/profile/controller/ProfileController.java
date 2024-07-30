package com.sparta.studytrek.domain.profile.controller;

import com.sparta.studytrek.domain.profile.dto.ProfileRequestDto;
import com.sparta.studytrek.domain.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;

	/**
	 *  프로필 등록 ( 로그인 한 유저의 부트캠프 정보를 입력받고 등록합니다.)
	 *
	 * @param userDetails
	 * @param requestDto
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> createProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ProfileRequestDto requestDto) {
		return profileService.createProfile(userDetails.getUsername(), requestDto);
	}

	/**
	 *  프로필 조회 ( 로그인 한 유저의 부트캠프 정보를 다 불러옵니다. )
	 *
	 * @param userDetails
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> getProfileByUserId(@AuthenticationPrincipal UserDetails userDetails) {
		return profileService.getProfileByUserId(userDetails.getUsername());
	}

	/**
	 *  프로필 수정 ( 로그인 한 유저가 등록한 부트캠프 정보를 수정합니다. )
	 *
	 * @param profileId
	 * @param requestDto
	 * @return
	 */
	@PutMapping("/{profileId}")
	public ResponseEntity<?> updateProfile(@PathVariable Long profileId, @RequestBody ProfileRequestDto requestDto) {
		return profileService.updateProfile(profileId, requestDto);
	}

	/**
	 *  프로필 삭제
	 *
	 * @param profileId
	 * @return
	 */
	@DeleteMapping("/{profileId}")
	public ResponseEntity<?> deleteProfile(@PathVariable Long profileId) {
		return profileService.deleteProfile(profileId);
	}
}
