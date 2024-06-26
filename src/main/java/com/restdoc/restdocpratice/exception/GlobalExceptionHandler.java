package com.restdoc.restdocpratice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class  GlobalExceptionHandler {

    @ExceptionHandler({CustomRuntimeException.class})
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomRuntimeException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode(), e.getAddMessage());
    }


}
