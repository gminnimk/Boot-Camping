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
class ChatServiceImplTest {

	@Mock
	private ChatRepository chatRepository;

	@Mock
	private ChatMapper chatMapper;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ChatServiceImpl chatService;

	private ChatMessageRequestDto requestDto;
	private User user;
	private Chat chat;
	private ChatMessageResponseDto responseDto;

	@BeforeEach
	void setUp() {
		requestDto = new ChatMessageRequestDto("Hello", "asdasd1");
		user = new User("asdasd1","Password1!","일반사용자", "대한민국 어딘가", UserType.NORMAL, new Role(UserRoleEnum.USER));
		chat = new Chat("Hello", "asdasd1", "일반사용자");
		responseDto = new ChatMessageResponseDto(1L, "Hello", "asdasd1", LocalDateTime.now(), "일반사용자");

		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(chatRepository.save(any(Chat.class))).thenReturn(chat);
		when(chatMapper.toChatMessageResponseDto(any(Chat.class))).thenReturn(responseDto);
	}

	@Test
	void saveMessage() {
		ChatMessageResponseDto result = chatService.saveMessage(requestDto);

		verify(userRepository, times(1)).findByUsername(anyString());
		verify(chatRepository, times(1)).save(any(Chat.class));
		verify(chatMapper, times(1)).toChatMessageResponseDto(any(Chat.class));

		assertEquals("asdasd1", result.getUsername());
		assertEquals("일반사용자", result.getName());
		assertEquals("Hello", result.getMessage());
	}

	@Test
	void saveMessageUserNotFound() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		CustomException exception = assertThrows(CustomException.class,
			() -> {
			chatService.saveMessage(requestDto);
			});

		assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
	}
}
