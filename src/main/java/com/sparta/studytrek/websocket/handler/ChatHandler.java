package com.sparta.studytrek.websocket.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.studytrek.domain.chat.dto.ChatMessageRequestDto;
import com.sparta.studytrek.domain.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

	private final ChatService chatService;
	private final ObjectMapper objectMapper;
	private final List<WebSocketSession> sessions = new ArrayList<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String username = getUsernameFromQuery(session);
		if (username != null) {
			session.getAttributes().put("username", username);
			sessions.add(session);
			log.info("WebSocket 연결 설정: username = " + username);
		} else {
			throw new IllegalStateException("세션에 username이 설정되지 않았습니다.");
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
		log.info("WebSocket 연결이 종료되었습니다: " + session.getId());
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.info("Received chat message: {}", payload);

		ChatMessageRequestDto chatMessage = objectMapper.readValue(payload, ChatMessageRequestDto.class);

		chatService.saveMessage(chatMessage);

		for (WebSocketSession webSocketSession : sessions) {
			webSocketSession.sendMessage(new TextMessage(payload));
		}
	}

	private String getUsernameFromQuery(WebSocketSession session) {
		String query = session.getUri().getQuery();
		if (query != null && query.contains("username=")) {
			for (String param : query.split("&")) {
				if (param.startsWith("username=")) {
					return param.split("=")[1];
				}
			}
		}
		return null;
	}
}
