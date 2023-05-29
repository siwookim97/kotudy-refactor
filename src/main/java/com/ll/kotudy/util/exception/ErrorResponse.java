package com.ll.kotudy.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private Timestamp timeStamp;
    private HttpStatus httpStatus;
    private int errorCode;
    private String msg;

    public static ErrorResponse occurred(final AppException e) {
        return new ErrorResponse(new Timestamp(System.currentTimeMillis()),
                e.getErrorCode().getHttpStatus(),
                e.getErrorCode().getCode(),
                e.getMsg());
    }

    public static ErrorResponse occurred(HttpStatus httpStatus, int errorCode, String msg) {
        return new ErrorResponse(new Timestamp(System.currentTimeMillis()),
                httpStatus,
                errorCode,
                msg);
    }
}
