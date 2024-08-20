package com.sparta.studytrek.notification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.studytrek.notification.entity.Notification;
import com.sparta.studytrek.notification.entity.QNotification;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

	public final JPAQueryFactory jpaQueryFactory;


	@Override
	public Page<Notification> findByUsernameWithQueryDSL(String username, Pageable pageable) {
		QNotification notification = QNotification.notification;

		List<Notification> notifications = jpaQueryFactory.selectFrom(notification)
			.where(notification.username.eq(username))
			.orderBy(notification.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = Optional.ofNullable(
			jpaQueryFactory.select(notification.count())
				.from(notification)
				.where(notification.username.eq(username))
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(notifications, pageable, total);
	}
}
