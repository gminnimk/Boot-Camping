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

	public void sendNotificationToUser(String username, String message) throws IOException {
		for (WebSocketSession session : sessions) {
			String sessionUsername = (String) session.getAttributes().get("username");
			System.out.println("세션에 저장된 username: " + sessionUsername);
			System.out.println("발송 대상 username: " + username);
			if (sessionUsername != null && sessionUsername.equals(username)) {
				session.sendMessage(new TextMessage(message));
				System.out.println("메시지를 전송했습니다: " + message + " to " + username);
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
