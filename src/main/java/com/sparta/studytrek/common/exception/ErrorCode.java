package com.sparta.studytrek.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "실패했습니다."),

    // Token
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명입니다."),
    TOKEN_EXPIRATION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다. 재로그인 해주세요."),
    NOT_SUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    FALSE_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다."),
    HEADER_NOT_FOUND_AUTH(HttpStatus.BAD_REQUEST, "권한 헤더가 잘못되었거나 누락되었습니다."),
    TOKEN_VALIDATE(HttpStatus.BAD_REQUEST, "Invalid JWT token."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    UNMATCHED_TOKEN(HttpStatus.BAD_REQUEST, "일치하지 않는 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "등록되지 않은 토큰입니다."),

    // User
    INVALID_USERNAME(HttpStatus.BAD_REQUEST,"아이디는 최소 6자 이상, 20자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"최소 8자 이상, 15자 이하이며 알파벳 대소문자(az, AZ), 숫자(0~9),특수문자로 구성되어야 합니다."),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "등록되지 않은 사용자입니다."),
    INCORRECT_USER(HttpStatus.BAD_REQUEST,"사용자가 동일하지 않습니다."),
    DUPLICATE_USER(HttpStatus.BAD_REQUEST,"이미 등록된 사용자 입니다."),
    WITHDRAW_USER(HttpStatus.BAD_REQUEST, "탈퇴한 회원입니다."),
    BANNED_USER(HttpStatus.FORBIDDEN, "BAN 처리된 사용자입니다."),
    BAD_MANAGER_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 암호입니다."),
    HEADER_NOT_FOUND_REFRESH(HttpStatus.BAD_REQUEST,"헤더에 토큰이 존재하지 않습니다."),
    USER_NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다. 로그인해주세요."),
    STATUS_NOT_FOUND(HttpStatus.BAD_REQUEST, "상태를 찾을 수 없습니다."),

    //Question
    NOTFOUND_QUESTION(HttpStatus.BAD_REQUEST, "해당 질문은 존재하지 않습니다.");

    private HttpStatus httpStatus;
    private String msg;
}
