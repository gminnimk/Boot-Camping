package com.sparta.studytrek.domain.admin.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sparta.studytrek.domain.admin.dto.AdminRequestDto;
import com.sparta.studytrek.domain.admin.dto.AdminResponseDto;
import com.sparta.studytrek.domain.admin.entity.Admin;

@Mapper(componentModel = "spring")
public interface AdminMapper {

	@Mapping(source = "id", target = "id")
	@Mapping(source = "username", target = "username")
	@Mapping(source = "adminToken", target = "adminToken")
	AdminResponseDto toAdminResponse(Admin admin);

	@Mapping(source = "username", target = "username")
	@Mapping(source = "password", target = "password")
	@Mapping(source = "adminToken", target = "adminToken")
	Admin toAdmin(AdminRequestDto adminRequestDto);

	List<AdminResponseDto> toAdminResponseList(List<Admin> admins);
}
