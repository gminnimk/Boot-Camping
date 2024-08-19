package com.sparta.studytrek.domain.chat.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
public class ChatServiceImpl implements ChatService {

	private final ChatRepository chatRepository;
	private final ChatMapper chatMapper;
	private final UserRepository userRepository;
	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

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

		sendMessageToAll(savedChat);

		return chatMapper.toChatMessageResponseDto(savedChat);
	}

	/**
	 * 모든 채팅 조회
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
	 * SSE Emitter 생성 및 관리
	 *
	 * @param username 사용자 이름
	 * @return SseEmitter 객체
	 */
	@Override
	public SseEmitter createEmitter(String username) {
		SseEmitter emitter = new SseEmitter(300000L);
		emitters.put(username, emitter);

		emitter.onCompletion(() -> emitters.remove(username));
		emitter.onTimeout(() -> emitters.remove(username));
		emitter.onError((e) -> emitters.remove(username));

		return emitter;
	}

	/**
	 * 실시간 메시지를 모든 연결된 클라이언트에게 전송
	 *
	 * @param chat 전송할 채팅 메시지
	 */
	private void sendMessageToAll(Chat chat) {
		List<SseEmitter> deadEmitters = emitters.values().stream()
			.filter(emitter -> {
				try {
					emitter.send(SseEmitter.event().data(chatMapper.toChatMessageResponseDto(chat)));
					return false;
				} catch (IOException e) {
					return true;
				}
			})
			.collect(Collectors.toList());

		deadEmitters.forEach(emitter -> emitters.values().remove(emitter));
	}

	/**
	 * 특정 채팅 메세지 삭제
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
