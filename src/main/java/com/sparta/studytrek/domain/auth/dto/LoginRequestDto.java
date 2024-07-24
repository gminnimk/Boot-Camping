package com.sparta.studytrek.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto {
    private Long id;
    private String username;
    private String password;
}