package com.sparta.studytrek.domain.chat.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ChatMessageResponseDto {
	private final Long id;
	private final String message;
	private final String username;
	private final LocalDateTime createdAt;

	public ChatMessageResponseDto(Long id, String message, String username, LocalDateTime createdAt) {
		this.id = id;
		this.message = message;
		this.username = username;
		this.createdAt = createdAt;
	}
}
