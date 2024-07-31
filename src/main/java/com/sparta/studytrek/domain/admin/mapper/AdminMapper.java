package com.sparta.studytrek.domain.admin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.sparta.studytrek.domain.admin.dto.AdminRequestDto;
import com.sparta.studytrek.domain.admin.dto.AdminResponseDto;
import com.sparta.studytrek.domain.auth.entity.User;

@Mapper(componentModel = "spring")
public interface AdminMapper {

	@Mappings({
		@Mapping(target = "role", expression = "java(com.sparta.studytrek.domain.auth.entity.UserRoleEnum.ADMIN)"),
		@Mapping(target = "userType", expression = "java(com.sparta.studytrek.domain.auth.entity.UserType.NORMAL)")
	})
	User toUser(AdminRequestDto dto);

	@Mappings({
		@Mapping(source = "id", target = "id"),
		@Mapping(source = "username", target = "username"),
		@Mapping(source = "name", target = "name")
	})
	AdminResponseDto toAdminResponseDto(User user);
}
