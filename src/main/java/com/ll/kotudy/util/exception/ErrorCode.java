package com.ll.kotudy.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, ""),
    USERNAME_BAD_REQUEST(HttpStatus.BAD_REQUEST, ""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, ""),
    PASSWORD_BAD_REQUEST(HttpStatus.BAD_REQUEST, "");

    private HttpStatus httpStatus;
    private String message;
}
