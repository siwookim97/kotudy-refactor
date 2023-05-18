package com.ll.kotudy.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private HttpStatus httpStatus;
    private int errorCode;
    private String msg;

    public static ErrorResponse occurred(final AppException e) {
        return new ErrorResponse(e.getErrorCode().getHttpStatus(), e.getErrorCode().getCode(), e.getMsg());
    }

    public static ErrorResponse occurred(HttpStatus httpStatus, int errorCode, String msg) {
        return new ErrorResponse(httpStatus, errorCode, msg);
    }
}
