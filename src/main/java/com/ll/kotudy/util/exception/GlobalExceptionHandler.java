package com.ll.kotudy.util.exception;

import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "서버에서 에러가 발생했습니다.";
    private static final String REQUEST_BODY_FORMAT_MESSAGE = "올바른 요청 Body 형식이 아닙니다.";
    private static final String REQUEST_PARAM_ERROR_MESSAGE = "올바른 요청 Parameter 형식이 아닙니다.";

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> appExceptionHandler(AppException e) {
        log.info(LOG_FORMAT, e.getClass().getSimpleName(),
                e.getErrorCode().getCode(),
                e.getMessage());

        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ErrorResponse.occurred(e));
    }

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ErrorResponse> runtimeExceptionHandler(RuntimeException e) {
//        log.info(LOG_FORMAT,
//                e.getClass().getSimpleName(),
//                ErrorCode.RUNTIME_EXCEPTION.getCode(),
//                REQUEST_BODY_FORMAT_MESSAGE);
//
//        return ResponseEntity.status(ErrorCode.RUNTIME_EXCEPTION.getHttpStatus())
//                .body(ErrorResponse.occurred(new AppException(
//                        ErrorCode.RUNTIME_EXCEPTION,
//                        INTERNAL_SERVER_ERROR_MESSAGE
//                )));
//    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> MissingServletRequestParameterExceptionHandler(
            MissingServletRequestParameterException e) {
        log.info(LOG_FORMAT,
                e.getClass().getSimpleName(),
                ErrorCode.REQUEST_PARAM_BAD_REQUEST.getCode(),
                e.getMessage());

        return ResponseEntity.status(ErrorCode.REQUEST_PARAM_BAD_REQUEST.getHttpStatus())
                .body(ErrorResponse.occurred(new AppException(
                        ErrorCode.REQUEST_PARAM_BAD_REQUEST,
                        REQUEST_PARAM_ERROR_MESSAGE
                )));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.info(LOG_FORMAT,
                e.getClass().getSimpleName(),
                ErrorCode.BODY_BAD_REQUEST.getCode(),
                e.getMessage());

        return ResponseEntity.status(ErrorCode.BODY_BAD_REQUEST.getHttpStatus())
                .body(makeValidErrorResponse(e.getBindingResult()));
    }

    private ErrorResponse makeValidErrorResponse(BindingResult bindingResult) {
        HttpStatus httpStatus = HttpStatus.OK;
        int errorCode = 200;
        String msg = "";

        if (bindingResult.hasErrors()) {
            msg = bindingResult.getFieldError().getDefaultMessage();

            String bindResultCode = bindingResult.getFieldError().getCode();

            switch(bindResultCode) {
                case "NotEmpty":
                    errorCode = ErrorCode.BODY_BAD_REQUEST.getCode();
                    httpStatus = ErrorCode.BODY_BAD_REQUEST.getHttpStatus();
                    break;
            }
        }

        return ErrorResponse.occurred(httpStatus, errorCode, msg);
    }
}
