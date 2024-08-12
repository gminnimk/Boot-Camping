package com.sparta.studytrek.websocket.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.studytrek.websocket.entity.Notification;
import com.sparta.studytrek.websocket.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping
	public ResponseEntity<?> getNotifications(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(defaultValue = "0") Integer page,
		@RequestParam(defaultValue = "10") Integer size
	)
	{
		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
		}
		try {
			String username = userDetails.getUsername();
			Page<Notification> notificationsPage = notificationService.getNotificationsForUser(username, page, size);
			return ResponseEntity.ok(notificationsPage);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching notifications: " + e.getMessage());
		}
	}

	@PostMapping("/{id}/read")
	public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id) {
		notificationService.markNotificationAsRead(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/list")
	public String listNotifications(Model model, @AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(defaultValue = "0") Integer page,
		@RequestParam(defaultValue = "10") Integer size)
	{
		String username = userDetails.getUsername();
		Page<Notification> notificationsPage = notificationService.getNotificationsForUser(username, page, size);

		model.addAttribute("notifications", notificationsPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", notificationsPage.getTotalPages());

		return "alarm";
	}

	@GetMapping("/unread-count")
	public ResponseEntity<Long> getUnreadNotificationCount(@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(0L);
		}
		String username = userDetails.getUsername();
		long unreadCount = notificationService.countUnreadNotificationsForUser(username);
		return ResponseEntity.ok(unreadCount);
	}
}
