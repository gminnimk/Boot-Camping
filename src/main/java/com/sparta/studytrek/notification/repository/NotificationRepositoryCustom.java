package com.sparta.studytrek.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sparta.studytrek.notification.entity.Notification;

public interface NotificationRepositoryCustom {
	Page<Notification> findByUsernameWithQueryDSL(String username, Pageable pageable);
}
