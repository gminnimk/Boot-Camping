package com.sparta.studytrek.domain.profile.controller;

import com.sparta.studytrek.domain.profile.dto.ProfileRequestDto;
import com.sparta.studytrek.domain.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;

	@PostMapping
	public ResponseEntity<?> createProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ProfileRequestDto requestDto) {
		return profileService.createProfile(userDetails.getUsername(), requestDto);
	}

	@GetMapping
	public ResponseEntity<?> getProfileByUserId(@AuthenticationPrincipal UserDetails userDetails) {
		return profileService.getProfileByUserId(userDetails.getUsername());
	}

	@PutMapping("/{profileId}")
	public ResponseEntity<?> updateProfile(@PathVariable Long profileId, @RequestBody ProfileRequestDto requestDto) {
		return profileService.updateProfile(profileId, requestDto);
	}

	@DeleteMapping("/{profileId}")
	public ResponseEntity<?> deleteProfile(@PathVariable Long profileId) {
		return profileService.deleteProfile(profileId);
	}
}
