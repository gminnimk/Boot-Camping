package com.sparta.studytrek.domain.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
