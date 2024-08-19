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

	@Mock
	private NotificationRepository notificationRepository;

	@InjectMocks
	private NotificationService notificationService;

	private Pageable pageable;
	private List<Notification> notifications;
	private Page<Notification> notificationPage;

	@BeforeEach
	void setUp() {
		Notification notification1 = new Notification("asdasd1", "test Message1");
		Notification notification2 = new Notification("asdasd1", "test Message2");
		notifications = List.of(notification1, notification2);

		pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
		notificationPage = new PageImpl<>(notifications, pageable, notifications.size());

		when(notificationRepository.findByUsername(eq("asdasd1"), any(Pageable.class))).thenReturn(notificationPage);
	}

	@Test
	void getNotificationsForUser() {
		Page<Notification> page = notificationService.getNotificationsForUser("asdasd1", 0, 10);

		verify(notificationRepository, times(1)).findByUsername(eq("asdasd1"), any(Pageable.class));
		assertNotNull(page);
		assertEquals(2, page.getTotalElements());
		assertEquals("test Message1", page.getContent().get(0).getMessage());
		assertEquals("test Message2", page.getContent().get(1).getMessage());
	}

	@Test
	void getNotificationsForUserWithNullPageAndSize() {
		Page<Notification> page = notificationService.getNotificationsForUser("asdasd1", null, null);

		verify(notificationRepository, times(1)).findByUsername(eq("asdasd1"), any(Pageable.class));
		assertNotNull(page);
		assertEquals(2, page.getTotalElements());
	}
}