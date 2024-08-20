package com.sparta.studytrek.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequestDto {
	private String message;
	private String username;
	private Long cursor;

	public ChatMessageRequestDto(String message, String username, Long cursor) {
		this.message = message;
		this.username = username;
		this.cursor = cursor;
	}
}
