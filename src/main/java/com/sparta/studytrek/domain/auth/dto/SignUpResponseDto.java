package com.sparta.studytrek.domain.auth.dto;

import com.sparta.studytrek.domain.auth.entity.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpResponseDto {
    String username;
    String name;
    String userAddr;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;
    String userStatusEnum;
    String userType;

    @Builder
    public SignUpResponseDto(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.userAddr = user.getUserAddr();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
        this.userStatusEnum = user.getStatus().getUserStatus();
        this.userType = user.getUserType().getUserType();
    }
}
