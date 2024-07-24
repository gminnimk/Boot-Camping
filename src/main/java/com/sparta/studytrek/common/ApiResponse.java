package com.sparta.studytrek.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String msg;
    private String statuscode;
    private T data;

    public ApiResponse(String msg, String statuscode, T data) {
        this.msg = msg;
        this.statuscode = statuscode;
        this.data = data;
    }

}
