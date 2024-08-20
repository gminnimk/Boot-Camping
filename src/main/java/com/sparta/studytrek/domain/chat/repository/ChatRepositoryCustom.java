package com.sparta.studytrek.domain.chat.repository;

import java.util.List;

import com.sparta.studytrek.domain.chat.entity.Chat;

public interface ChatRepositoryCustom {
	List<Chat> findAllWithCursor(Long cursor, int size);
}
