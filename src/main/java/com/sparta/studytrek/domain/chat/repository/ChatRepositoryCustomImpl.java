package com.sparta.studytrek.domain.chat.repository;

import static com.sparta.studytrek.domain.chat.entity.QChat.*;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.studytrek.domain.chat.entity.Chat;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Chat> findAllWithCursor(Long cursor, int size) {
		List<Chat> chats =  queryFactory.selectFrom(chat)
			.where(cursor != null ? chat.id.lt(cursor) : null)
			.orderBy(chat.id.desc())
			.limit(size)
			.fetch();

		Collections.reverse(chats);
		return chats;
	}
}
