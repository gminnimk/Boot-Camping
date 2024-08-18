package com.sparta.studytrek.domain.chat.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sparta.studytrek.domain.chat.dto.ChatMessageRequestDto;
import com.sparta.studytrek.domain.chat.dto.ChatMessageResponseDto;

public interface ChatService {
	@Transactional
	ChatMessageResponseDto saveMessage(ChatMessageRequestDto requestDto);

	@Transactional(readOnly = true)
	List<ChatMessageResponseDto> getAllMessages();

	SseEmitter createEmitter(String username);

	@Transactional
	void deleteMessage(Long chatId);
}
