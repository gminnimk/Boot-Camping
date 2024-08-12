package com.sparta.studytrek.websocket.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.sparta.studytrek.websocket.entity.Notification;
import com.sparta.studytrek.websocket.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final List<WebSocketSession> sessions = new ArrayList<>();
	private final Map<String, List<String>> userNotifications = new HashMap<>();
	private final NotificationRepository notificationRepository;

	public void addSession(WebSocketSession session) {
		sessions.add(session);
	}

	public void removeSession(WebSocketSession session) {
		sessions.remove(session);
	}

	public void sendNotificationToUser(String username, String message) throws IOException {
		Notification notification = new Notification(username, message);
		notificationRepository.save(notification);

		userNotifications.computeIfAbsent(username, k -> new ArrayList<>()).add(message);

		for (WebSocketSession session : sessions) {
			String sessionUsername = (String) session.getAttributes().get("username");
			if (sessionUsername != null && sessionUsername.equals(username)) {
				session.sendMessage(new TextMessage(message));
				break;
			}
		}
	}

	public void sendNotificationToAll(String message) throws IOException {
		TextMessage textMessage = new TextMessage(message);
		for (WebSocketSession session : sessions) {
			String username = (String) session.getAttributes().get("username");
			if (username != null) {
				Notification notification = new Notification(username, message);
				notificationRepository.save(notification);
				userNotifications.computeIfAbsent(username, k -> new ArrayList<>()).add(message);
			}
			session.sendMessage(textMessage);
		}
	}

	public List<Notification> getNotificationsForUser(String username) {
		return notificationRepository.findByUsername(username);
	}

	public void markNotificationAsRead(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new RuntimeException("Notification not found"));
		notification.markAsRead();
		notificationRepository.save(notification);
	}

	public void deleteNotification(Long notificationId) {
		notificationRepository.deleteById(notificationId);
	}

	public void deleteAllNotificationsForUser(String username) {
		notificationRepository.deleteByUsername(username);
		userNotifications.remove(username);
	}
}