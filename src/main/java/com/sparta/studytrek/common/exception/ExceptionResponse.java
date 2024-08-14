package com.sparta.studytrek.common.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {
    private boolean success;
    private String msg;

    public ExceptionResponse(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }
}