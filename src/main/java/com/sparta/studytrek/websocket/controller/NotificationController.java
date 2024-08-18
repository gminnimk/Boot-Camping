package com.sparta.studytrek.websocket.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sparta.studytrek.jwt.JwtUtil;
import com.sparta.studytrek.websocket.entity.Notification;
import com.sparta.studytrek.websocket.service.NotificationService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;
	private final JwtUtil jwtUtil;

	@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter streamNotifications(@RequestParam String username, @RequestParam String token) {

		if (!jwtUtil.validateToken(token)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
		}

		try {
			SseEmitter emitter = notificationService.createEmitter(username);

			emitter.send(SseEmitter.event().name("connect").data("SSE 연결 성공"));

			return emitter;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SSE 연결 중 오류 발생");
		}
	}

	@PostMapping("/{id}/read")
	public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id) {
		notificationService.markNotificationAsRead(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/list")
	public ResponseEntity<Page<Notification>> listNotifications(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(defaultValue = "0") Integer page,
		@RequestParam(defaultValue = "10") Integer size) {

		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String username = userDetails.getUsername();
		Page<Notification> notificationsPage = notificationService.getNotificationsForUser(username, page, size);

		return ResponseEntity.ok(notificationsPage);
	}

	@GetMapping("/unread-count/{username}")
	public ResponseEntity<Long> getUnreadNotificationCount(@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable String username) {
		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		long unreadCount = notificationService.countUnreadNotificationsForUser(username);
		return ResponseEntity.ok(unreadCount);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteNotification(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		try {
			Notification notification = notificationService.getNotificationById(id);
			if (!notification.getUsername().equals(userDetails.getUsername())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

			notificationService.deleteNotification(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(null);
		}
	}
}