package com.ll.kotudy.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USERNAME_DUPLICATED(409, HttpStatus.CONFLICT),
    BODY_BAD_REQUEST(400, HttpStatus.BAD_REQUEST),
    REQUEST_PARAM_BAD_REQUEST(400, HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(401, HttpStatus.UNAUTHORIZED),
    RUNTIME_EXCEPTION(500, HttpStatus.INTERNAL_SERVER_ERROR);

    private int code;
    private HttpStatus httpStatus;
}