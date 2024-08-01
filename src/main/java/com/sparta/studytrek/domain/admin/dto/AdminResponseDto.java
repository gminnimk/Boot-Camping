package com.sparta.studytrek.domain.admin.dto;

import java.time.LocalDateTime;

import com.sparta.studytrek.domain.auth.entity.UserRoleEnum;
import com.sparta.studytrek.domain.auth.entity.UserStatusEnum;

public record AdminResponseDto(Long id, String username, String name, LocalDateTime createdAt, LocalDateTime modifiedAt, String userType) {}