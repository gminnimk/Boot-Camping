package com.sparta.studytrek.common.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {
    private boolean success;
    private String message;

    public ExceptionResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
