package com.sparta.studytrek.websocket.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.w3c.dom.Text;

@Service
public class NotificationService {

	private final List<WebSocketSession> sessions = new ArrayList<>();

	public void addSession(WebSocketSession session) {
		sessions.add(session);
	}

	public void removeSession(WebSocketSession session) {
		sessions.remove(session);
	}

	public void sendNotificationToUser(Long userId, String message) throws IOException {
		for (WebSocketSession session : sessions) {
			if (session.getAttributes().get("userId").equals(userId)) {
				session.sendMessage(new TextMessage(message));
				break;
			}
		}
	}

	public void sendNotificationToAll(String message) throws IOException {
		TextMessage textMessage = new TextMessage(message);
		for (WebSocketSession session : sessions) {
			session.sendMessage(textMessage);
		}
	}
}
