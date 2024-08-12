package com.sparta.studytrek.domain.chat.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sparta.studytrek.domain.chat.dto.ChatMessageRequestDto;
import com.sparta.studytrek.domain.chat.dto.ChatMessageResponseDto;

public interface ChatService {
	@Transactional
	ChatMessageResponseDto saveMessage(ChatMessageRequestDto requestDto);

	@Transactional(readOnly = true)
	List<ChatMessageResponseDto> getAllMessages();

	@Transactional
	void deleteMessage(Long chatId);
}
