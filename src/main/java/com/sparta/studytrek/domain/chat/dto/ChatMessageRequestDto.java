package com.sparta.studytrek.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequestDto {
	private String message;
	private String username;

	public ChatMessageRequestDto(String message, String username) {
		this.message = message;
		this.username = username;
	}
}
