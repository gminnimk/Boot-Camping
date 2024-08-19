package com.sparta.studytrek.notification.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sparta.studytrek.common.exception.CustomException;
import com.sparta.studytrek.common.exception.ErrorCode;
import com.sparta.studytrek.notification.entity.Notification;
import com.sparta.studytrek.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

	private final Map<String, SseEmitter> emitters = new HashMap<>();
	private final NotificationRepository notificationRepository;

	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 10;

	@Transactional
	public SseEmitter createEmitter(String username) {
		SseEmitter emitter = new SseEmitter(300000L);
		emitters.put(username, emitter);

		emitter.onCompletion(() -> emitters.remove(username));
		emitter.onTimeout(() -> {
			emitters.remove(username);
			emitter.complete();
		});
		emitter.onError(e -> emitters.remove(username));

		return emitter;
	}

	@Transactional
	public void createAndSendNotification(String username, String message) throws IOException {
		Notification notification = new Notification(username, message);

		notificationRepository.save(notification);

		sendNotificationToUser(username, message);
	}


	public void sendNotificationToUser(String username, String message) throws IOException {
		SseEmitter emitter = emitters.get(username);
		if (emitter != null) {
			emitter.send(SseEmitter.event()
				.name("message")
				.data(message));
		} else {
			log.warn("사용자 {}에 대한 Emitter를 찾을 수 없습니다", username);
		}
	}



	public void sendNotificationToAll(String message) throws IOException {
		for (String username : emitters.keySet()) {
			sendNotificationToUser(username, message);
		}
	}

	public Page<Notification> getNotificationsForUser(String username, Integer page, Integer size) {
		int actualPage = (page == null) ? DEFAULT_PAGE : page;
		int actualSize = (size == null) ? DEFAULT_SIZE : size;

		Pageable pageable = PageRequest.of(actualPage, actualSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		return notificationRepository.findByUsername(username, pageable);
	}

	public void markNotificationAsRead(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId).orElseThrow(
			() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND)
		);
		notification.markAsRead();
		notificationRepository.save(notification);
	}

	public void deleteNotification(Long notificationId) {
		notificationRepository.deleteById(notificationId);
	}

	public void deleteAllNotificationsForUser(String username) {
		notificationRepository.deleteByUsername(username);
	}

	public long countUnreadNotificationsForUser(String username) {
		return notificationRepository.countByUsernameAndIsReadFalse(username);
	}

	public Notification getNotificationById(Long id) {
		return notificationRepository.findById(id).orElseThrow(
			() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND)
		);
	}
}
