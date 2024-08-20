package com.sparta.studytrek.domain.chat.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ChatMessageResponseDto {
	private final Long id;
	private final String message;
	private final String username;
	private final LocalDateTime createdAt;
	private final String name;
	private final Long nextCursor;

	public ChatMessageResponseDto(Long id, String message, String username, LocalDateTime createdAt, String name, Long nextCursor) {
		this.id = id;
		this.message = message;
		this.username = username;
		this.createdAt = createdAt;
		this.name = name;
		this.nextCursor = nextCursor;
	}
}
