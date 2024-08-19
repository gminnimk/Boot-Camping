package com.sparta.studytrek.domain.chat.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sparta.studytrek.common.ApiResponse;
import com.sparta.studytrek.common.ResponseText;
import com.sparta.studytrek.domain.chat.dto.ChatMessageRequestDto;
import com.sparta.studytrek.domain.chat.dto.ChatMessageResponseDto;
import com.sparta.studytrek.domain.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	/**
	 * 채팅 메세지 생성 API
	 *
	 * @param requestDto 채팅 메세지 생성 데이터
	 * @return 생성된 채팅 메세지 응답 데이터
	 */
	@PostMapping
	public ResponseEntity<ApiResponse> sendMessage(@RequestBody ChatMessageRequestDto requestDto) {
		ChatMessageResponseDto responseDto = chatService.saveMessage(requestDto);
		ApiResponse response = ApiResponse.builder()
			.msg(ResponseText.CHAT_CREATE_SUCCESS.getMsg())
			.statuscode(String.valueOf(HttpStatus.CREATED.value()))
			.data(responseDto)
			.build();
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 모든 채팅 메세지 조회 API
	 *
	 * @return 모든 채팅 메세지 응답
	 */
	@GetMapping
	public ResponseEntity<ApiResponse> getAllChats() {
		List<ChatMessageResponseDto> responseDto = chatService.getAllMessages();
		ApiResponse response = ApiResponse.builder()
			.msg(ResponseText.CHAT_GET_SUCCESS.getMsg())
			.statuscode(String.valueOf(HttpStatus.OK.value()))
			.data(responseDto)
			.build();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * SSE를 통해 실시간 채팅 메시지를 스트리밍하는 엔드포인트
	 *
	 * @param username 사용자 이름
	 * @return SseEmitter 객체
	 */
	@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter streamChat(@RequestParam String username) {
		return chatService.createEmitter(username);
	}

	/**
	 * 특정 채팅 메세지 삭제
	 *
	 * @param chatId 삭제할 채팅 메세지 ID
	 * @return 채팅 삭제 응답 데이터
	 */
	@DeleteMapping("/{chatId}")
	public ResponseEntity<ApiResponse> deleteChat(@PathVariable("chatId") Long chatId) {
		chatService.deleteMessage(chatId);
		ApiResponse response = ApiResponse.builder()
			.msg(ResponseText.CHAT_DELETE_SUCCESS.getMsg())
			.statuscode(String.valueOf(HttpStatus.NO_CONTENT.value()))
			.build();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
	}
}
