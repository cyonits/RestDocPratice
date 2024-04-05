package com.restdoc.restdocpratice.exception;

import lombok.Getter;

@Getter
public class CustomRuntimeException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String addMessage;

    public CustomRuntimeException(ErrorCode e, String addMessage){
        this.errorCode = e;
        this.addMessage = addMessage;
    }

    public CustomRuntimeException(ErrorCode e){
        this.errorCode = e;
        this.addMessage = null;
    }

}