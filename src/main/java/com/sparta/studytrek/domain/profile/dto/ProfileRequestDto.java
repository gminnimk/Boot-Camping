package com.sparta.studytrek.domain.profile.dto;

import java.time.LocalDate;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileRequestDto {
	private String bootcampName;
	private String track;
	private String generation;
	private LocalDate startDate;
	private LocalDate endDate;
	private Set<String> techStack;
	private String certificate;
}
