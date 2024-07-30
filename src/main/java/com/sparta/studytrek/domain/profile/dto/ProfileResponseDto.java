package com.sparta.studytrek.domain.profile.dto;

import java.time.LocalDate;
import java.util.Set;

import com.sparta.studytrek.domain.profile.entity.Profile;

import lombok.Getter;

@Getter
public class ProfileResponseDto {
	private Long id;
	private String bootcampName;
	private String track;
	private String generation;
	private LocalDate startDate;
	private LocalDate endDate;
	private Set<String> techStack;
	private String certificate;

	public ProfileResponseDto(Profile profile) {
		this.id = profile.getId();
		this.bootcampName = profile.getBootcampName();
		this.track = profile.getTrack();
		this.generation = profile.getGeneration();
		this.startDate = profile.getStartDate();
		this.endDate = profile.getEndDate();
		this.techStack = profile.getTechStack();
		this.certificate = profile.getCertificate();
	}
}
