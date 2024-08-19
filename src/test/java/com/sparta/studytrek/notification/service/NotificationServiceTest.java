package com.sparta.studytrek.notification.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.sparta.studytrek.notification.entity.Notification;
import com.sparta.studytrek.notification.repository.NotificationRepository;

@SpringBootTest
class NotificationServiceTest {

	private static final String USERNAME = "asdasd1";
	private static final String MESSAGE1 = "test Message1";
	private static final String MESSAGE2 = "test Message2";
	private static final int PAGE = 0;
	private static final int SIZE = 10;
	private static final Sort SORT = Sort.by(Sort.Direction.DESC, "createdAt");

	@Mock
	private NotificationRepository notificationRepository;

	@InjectMocks
	private NotificationService notificationService;

	@Test
	void getNotificationsForUser() {
		// Given
		Notification notification1 = new Notification(USERNAME, MESSAGE1);
		Notification notification2 = new Notification(USERNAME, MESSAGE2);
		List<Notification> notifications = List.of(notification1, notification2);
		Pageable pageable = PageRequest.of(PAGE, SIZE, SORT);
		Page<Notification> notificationPage = new PageImpl<>(notifications, pageable, notifications.size());

		when(notificationRepository.findByUsername(eq(USERNAME), any(Pageable.class))).thenReturn(notificationPage);

		// When
		Page<Notification> page = notificationService.getNotificationsForUser(USERNAME, PAGE, SIZE);

		// Then
		verify(notificationRepository, times(1)).findByUsername(eq(USERNAME), any(Pageable.class));
		assertNotNull(page);
		assertEquals(2, page.getTotalElements());
		assertEquals(notification1, page.getContent().get(0));
		assertEquals(notification2, page.getContent().get(1));
	}

	@Test
	void getNotificationsForUserWithNullPageAndSize() {
		// Given
		Notification notification1 = new Notification(USERNAME, MESSAGE1);
		Notification notification2 = new Notification(USERNAME, MESSAGE2);
		List<Notification> notifications = List.of(notification1, notification2);

		int defaultPage = 0;
		int defaultSize = 10;

		Pageable defaultPageable = PageRequest.of(defaultPage, defaultSize, SORT);
		Page<Notification> notificationPage = new PageImpl<>(notifications, defaultPageable, notifications.size());

		when(notificationRepository.findByUsername(eq(USERNAME), eq(defaultPageable))).thenReturn(notificationPage);

		// When
		Page<Notification> page = notificationService.getNotificationsForUser(USERNAME, null, null);

		// Then
		verify(notificationRepository, times(1)).findByUsername(eq(USERNAME), eq(defaultPageable));
		assertNotNull(page);
		assertEquals(2, page.getTotalElements());
	}
}