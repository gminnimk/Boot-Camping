package com.sparta.studytrek.domain.chat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.repository.UserRepository;
import com.sparta.studytrek.domain.chat.dto.ChatMessageRequestDto;
import com.sparta.studytrek.domain.chat.dto.ChatMessageResponseDto;
import com.sparta.studytrek.domain.chat.entity.Chat;
import com.sparta.studytrek.domain.chat.mapper.ChatMapper;
import com.sparta.studytrek.domain.chat.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{

	private final ChatRepository chatRepository;
	private final ChatMapper chatMapper;
	private final UserRepository userRepository;

	/**
	 * 채팅 메세지 저장
	 *
	 * @param requestDto 저장할 채팅 메세지
	 * @return 저장된 메세지를 반환
	 */
	@Override
	@Transactional
	public ChatMessageResponseDto saveMessage(ChatMessageRequestDto requestDto) {
		User user = userRepository.findByUsername(requestDto.getUsername())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		Chat chat = Chat.builder()
			.message(requestDto.getMessage())
			.username(requestDto.getUsername())
			.name(user.getName())
			.build();

		Chat savedChat = chatRepository.save(chat);

		return chatMapper.toChatMessageResponseDto(savedChat);
	}

	/**
	 *  모든 채팅 조회
	 *
	 * @return 모든 채팅 메세지를 Response 리스트로 반환
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ChatMessageResponseDto> getAllMessages() {
		List<Chat> chats = chatRepository.findAll();
		return chats.stream()
			.map(chatMapper::toChatMessageResponseDto)
			.collect(Collectors.toList());
	}

	/**
	 *  특정 채팅 메세지 삭제
	 *
	 * @param chatId
	 */
	@Override
	@Transactional
	public void deleteMessage(Long chatId) {
		Chat chat = chatRepository.findById(chatId).orElseThrow(
			() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND)
		);
		chatRepository.delete(chat);
	}
}