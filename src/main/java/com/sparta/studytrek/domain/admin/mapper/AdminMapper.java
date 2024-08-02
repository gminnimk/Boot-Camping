package com.sparta.studytrek.domain.admin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.sparta.studytrek.domain.admin.dto.AdminResponseDto;
import com.sparta.studytrek.domain.auth.entity.User;

@Mapper(componentModel = "spring")
public interface AdminMapper {

	@Mappings({
		@Mapping(source = "id", target = "id"),
		@Mapping(source = "username", target = "username"),
		@Mapping(source = "name", target = "name")
	})
	AdminResponseDto toAdminResponseDto(User user);
}
