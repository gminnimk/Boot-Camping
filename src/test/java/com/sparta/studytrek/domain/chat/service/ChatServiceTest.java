package com.sparta.studytrek.domain.chat.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.Role;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.entity.UserRoleEnum;
import com.sparta.studytrek.domain.auth.entity.UserType;
import com.sparta.studytrek.domain.auth.repository.UserRepository;
import com.sparta.studytrek.domain.chat.dto.ChatMessageRequestDto;
import com.sparta.studytrek.domain.chat.dto.ChatMessageResponseDto;
import com.sparta.studytrek.domain.chat.entity.Chat;
import com.sparta.studytrek.domain.chat.mapper.ChatMapper;
import com.sparta.studytrek.domain.chat.repository.ChatRepository;

@SpringBootTest
class ChatServiceTest {

	private static final String USERNAME = "asdasd1";
	private static final String PASSWORD = "Password1!";
	private static final String NAME = "일반사용자";
	private static final String ADDRESS = "대한민국 어딘가";
	private static final String MESSAGE = "Hello";
	private static final Long CHAT_ID = 1L;
	private static final LocalDateTime TIMESTAMP = LocalDateTime.now();

	@Mock
	private ChatRepository chatRepository;

	@Mock
	private ChatMapper chatMapper;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ChatServiceImpl chatService;

	@Test
	void saveMessage() {
		// Given
		ChatMessageRequestDto requestDto = new ChatMessageRequestDto(MESSAGE, USERNAME);
		User user = new User(USERNAME, PASSWORD, NAME, ADDRESS, UserType.NORMAL, new Role(UserRoleEnum.USER));
		Chat chat = new Chat(MESSAGE, USERNAME, NAME);
		ChatMessageResponseDto responseDto = new ChatMessageResponseDto(CHAT_ID, MESSAGE, USERNAME, LocalDateTime.now(), NAME);

		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(chatRepository.save(any(Chat.class))).thenReturn(chat);
		when(chatMapper.toChatMessageResponseDto(any(Chat.class))).thenReturn(responseDto);

		// When
		ChatMessageResponseDto result = chatService.saveMessage(requestDto);

		// Then
		verify(userRepository, times(1)).findByUsername(anyString());
		verify(chatRepository, times(1)).save(any(Chat.class));
		verify(chatMapper, times(1)).toChatMessageResponseDto(any(Chat.class));

		assertEquals(USERNAME, result.getUsername());
		assertEquals(NAME, result.getName());
		assertEquals(MESSAGE, result.getMessage());
	}

	@Test
	void saveMessageUserNotFound() {
		// Given
		ChatMessageRequestDto requestDto = new ChatMessageRequestDto(MESSAGE, USERNAME);

		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		// When
		CustomException exception = assertThrows(CustomException.class,
			() -> {
			chatService.saveMessage(requestDto);
			});

		// Then
		assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
	}
}
