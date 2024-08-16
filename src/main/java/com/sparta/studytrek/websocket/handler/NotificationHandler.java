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
		String username = getUsernameFromQuery(session);
		if (username != null) {
			session.getAttributes().put("username", username);
		} else {
			throw new IllegalStateException("세션에 username이 설정되지 않았습니다.");
		}
		notificationService.addSession(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		notificationService.removeSession(session);
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
