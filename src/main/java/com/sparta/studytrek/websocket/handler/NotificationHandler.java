package com.sparta.studytrek.websocket.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.sparta.studytrek.websocket.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationHandler extends TextWebSocketHandler {

	private final NotificationService notificationService;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		Long userId = getUserIdFromSession(session);
		session.getAttributes().put("userId", userId);

		notificationService.addSession(session);
		System.out.println("WebSocket 연결이 설정되었습니다: " + session.getId());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		notificationService.removeSession(session);
		System.out.println("WebSocket 연결이 종료되었습니다: " + session.getId());
	}

	private Long getUserIdFromSession(WebSocketSession session) {
		return 1L;
	}
}
