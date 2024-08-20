package com.sparta.studytrek.domain.chat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.sparta.studytrek.domain.chat.dto.ChatMessageRequestDto;
import com.sparta.studytrek.domain.chat.dto.ChatMessageResponseDto;
import com.sparta.studytrek.domain.chat.entity.Chat;

@Mapper(componentModel = "spring")
public interface ChatMapper {
	@Mappings({
		@Mapping(source = "id", target = "id"),
		@Mapping(source = "message", target = "message"),
		@Mapping(source = "username", target = "username"),
		@Mapping(source = "createdAt", target = "createdAt")
	})
	ChatMessageResponseDto toChatMessageResponseDto(Chat chat);
}
